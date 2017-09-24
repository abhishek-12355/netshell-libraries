package com.netshell.libraries.utilities.converter;

import com.netshell.libraries.utilities.common.CommonUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static com.netshell.libraries.utilities.common.Wrapper.wrapFunction;

/**
 * @author Abhishek
 * @since 26-06-2015.
 */
public final class Converters {
    static {
        registerConverters();
    }

    private Converters() {
        throw new UnsupportedOperationException();
    }

    private static void registerConverters() {
        registerConverter(
                StringToIntegerConverter.class,
                IntegerToBooleanConverter.class,
                StringToBooleanConverter.class,
                DateToXmlGregorianCalendarConverter.class);

        CommonUtils.loadList(Converter.class)
                .forEachRemaining(Converters::registerConverter);
    }

    public static void registerConverter(Class<? extends Converter>... converterClasses) {
        for (final Class<? extends Converter> converterClass : converterClasses) {
            CommonUtils.getManager().registerSingleton(
                    converterClass.getName(),
                    () -> wrapFunction(converterClass::newInstance));
        }
    }

    public static <C extends Converter> void registerConverter(C converter) {
        CommonUtils.getManager().registerSingleton(converter);
    }

    public static <C extends Converter> void registerConverter(C... converters) {
        for (final Converter ftConverter : converters) {
            CommonUtils.getManager().registerSingleton(ftConverter);
        }
    }

    public static <F, T> T convert(final Class<? extends Converter<F, T>> tConverterClass, final F student) {
        return Converters.<F, T>getConverter(tConverterClass).convert(student);
    }

    @SuppressWarnings("unchecked")
    public static <T, F> Converter<T, F> getConverter(Class<? extends Converter> converterClass) {
        return findConverter(converterClass).get();
    }

    public static Optional<Converter> findConverter(Class<? extends Converter> converterClass) {
        return CommonUtils.getManager().getSingleton(converterClass.getName(), Converter.class);
    }

    public static <F, T> F convertBack(final Class<? extends Converter<F, T>> tConverterClass, final T student) {
        return Converters.<F, T>getConverter(tConverterClass).convertBack(student);
    }

    public static final class StringToIntegerConverter implements Converter<String, Integer> {
        StringToIntegerConverter() {
        }

        @Override
        public Integer convert(String s) {
            return Integer.parseInt(s);
        }

        @Override
        public String convertBack(Integer integer) {
            return String.valueOf(integer);
        }
    }

    public static final class StringToBooleanConverter implements Converter<String, Boolean> {
        @Override
        public Boolean convert(String s) {
            return "true".equalsIgnoreCase(s);
        }

        @Override
        public String convertBack(Boolean aBoolean) {
            return Boolean.toString(aBoolean);
        }
    }

    public static final class IntegerToBooleanConverter implements Converter<Integer, Boolean> {
        @Override
        public Boolean convert(Integer integer) {
            return integer != 0;
        }

        @Override
        public Integer convertBack(Boolean aBoolean) {
            return aBoolean ? 1 : 0;
        }
    }

    public static final class DateToXmlGregorianCalendarConverter implements Converter<Date, XMLGregorianCalendar> {
        private static final DatatypeFactory datatypeFactory;

        static {
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(
                        "Error while trying to obtain a new instance of DataTypeFactory", e);
            }
        }

        @Override
        public XMLGregorianCalendar convert(Date date) {
            if (date == null) {
                return null;
            }

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return datatypeFactory.newXMLGregorianCalendar(gc);
        }

        @Override
        public Date convertBack(XMLGregorianCalendar xmlGregorianCalendar) {
            if (xmlGregorianCalendar == null) {
                return null;
            }
            return xmlGregorianCalendar.toGregorianCalendar().getTime();
        }
    }
}
