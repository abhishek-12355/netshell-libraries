package com.netshell.libraries.utilities.common;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Abhishek
 * @since 7/23/2016.
 */
public final class ReflectionUtils {
    /**
     * A default proxy handler. Based on the method it behaves as follows in order
     * <ol>
     * <li> when {@code method} is a default method, it invokes the default implementation
     * <li> when return type of {@code method} is a primitive type, the default value is returned {@link ReflectionUtils#defaultFromPrimitive(Class)}
     * <li> in all other cases null is returned
     * </ol>
     *
     * @param proxy  proxy object
     * @param method meethod invoked on proxy object
     * @param params parameters passed to method
     * @return see description
     */
    public static Object handleProxy(Object proxy, Method method, Object[] params) {
        if (method.isDefault()) {
            final Class<?> declaringClass = method.getDeclaringClass();
            try {
                final Constructor<MethodHandles.Lookup> constructor =
                        MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                        .unreflectSpecial(method, declaringClass)
                        .bindTo(proxy)
                        .invokeWithArguments(params);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        // proxy impl of not defaults methods

        if (method.getReturnType().isPrimitive()) {
            return defaultFromPrimitive(method.getReturnType());
        }

        return null;
    }

    /**
     * <ul>
     * <li>FALSE for boolean type
     * <li>0D for double
     * <li>0F for float
     * <li>0L for long
     * <li>0 for all other numeric or character type
     * <li>null otherwise
     * </ul>
     *
     * @param type type of primitive
     * @return {@code type}'s default value
     */
    public static Object defaultFromPrimitive(Class<?> type) {
        if (Byte.TYPE.equals(type)) return (byte) 0;
        else if (Short.TYPE.equals(type)) return (short) 0;
        else if (Character.TYPE.equals(type)) return (char) 0;
        else if (Integer.TYPE.equals(type)) return 0;
        else if (Long.TYPE.equals(type)) return 0L;
        else if (Double.TYPE.equals(type)) return 0D;
        else if (Float.TYPE.equals(type)) return 0F;
        else if (Boolean.TYPE.equals(type)) return false;
        return null;
    }

    /**
     * Create a proxy object having implementing all of {@code interfaces} and
     * invocation handler {@link ReflectionUtils#handleProxy(Object, Method, Object[])}
     * It uses the classloader of {@code tClass}
     *
     * @param tClass class object of type
     * @param <T>    type of object
     * @return a proxy object of type {@code tClass}
     */
    public static <T> T createIdentityProxy(final Class<T> tClass) {
        return tClass.cast(createProxy(tClass));
    }

    /**
     * Create a proxy object having implementing all of {@code interfaces} and
     * invocation handler {@link ReflectionUtils#handleProxy(Object, Method, Object[])}
     * It uses the classloader of the first interface in {@code interfaces}
     *
     * @param interfaces list of interfaces that this object would implement
     * @return a proxy object
     * @see ReflectionUtils#handleProxy(Object, Method, Object[])
     */
    public static Object createProxy(final Class... interfaces) {
        return createProxy(ReflectionUtils::handleProxy, interfaces);
    }

    /**
     * Create a proxy object having implementing all of {@code interfaces} and invocation handler {@code handler}
     * It uses the classloader of the first interface in {@code interfaces}
     *
     * @param handler    handler function that would be invoked when a method is called on this object.
     * @param interfaces list of interfaces that this object would implement
     * @return a proxy object
     */
    public static Object createProxy(final InvocationHandler handler, final Class... interfaces) {
        return Proxy.newProxyInstance(
                CommonMethods.checkInput(interfaces)[0].getClassLoader(), interfaces, handler);
    }
}
