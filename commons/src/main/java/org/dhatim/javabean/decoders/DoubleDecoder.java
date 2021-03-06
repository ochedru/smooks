package org.dhatim.javabean.decoders;

import org.dhatim.javabean.DataDecoder;
import org.dhatim.javabean.DataDecodeException;
import org.dhatim.javabean.DecodeType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Double decoder.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@DecodeType({Double.class, double.class})
public class DoubleDecoder extends NumberDecoder {

    public Object decode(String data) throws DataDecodeException {
        NumberFormat format = getNumberFormat();

        if(format != null) {
            try {
                Number number = format.parse(data.trim());
                return number.doubleValue();
            } catch (ParseException e) {
                throw new DataDecodeException("Failed to decode Double value '" + data + "' using NumberFormat instance " + format + ".", e);
            }
        } else {
            try {
                return Double.parseDouble(data.trim());
            } catch(NumberFormatException e) {
                throw new DataDecodeException("Failed to decode Double value '" + data + "'.", e);
            }
        }
    }
}
