package cn.itcast.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StringToDateConverter implements Converter<String,Date> {
    private final String[] FORMATS = {"yyyy-MM-dd","yyyy/MM/dd","yyyy年MM月dd日"};
    @Override
    public Date convert(String source) {
        for (int i = 0; i < FORMATS.length; i++) {
            try {
                // 判断：不为空再转换
                if (!StringUtils.isEmpty(source)) {
                    // 转换成功，直接返回
                    return new SimpleDateFormat(FORMATS[i]).parse(source);
                }
            } catch (ParseException e) {
                continue;
            }
        }
        return null;
    }
}
