package com.example.portfolio.filter;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.util.Strings;

@Getter
@Setter
public class QueryStringSupport {

  protected String sort;
  protected Integer page;

  public String getQueryString() {
    try {
      List<String> queries = new ArrayList<>();
      List<Field> fieldList = FieldUtils.getAllFieldsList(this.getClass());
      for (Field f : fieldList) {
        String key = f.getName();
        Object value = FieldUtils.readField(f, this, true);

        if (value == null) {
          // querys.add(key + "=");
          continue;
        }

        if (value instanceof Collection) {
          Collection col = (Collection) value;
          for (Object o : col) {
            queries.add(key + "=" + new URLCodec().encode(o.toString()));
          }
        } else {
          queries.add(key + "=" + new URLCodec().encode(value.toString()));
        }
      }

      return Strings.join(queries, '&');
    } catch (IllegalAccessException | EncoderException e) {
      return "";
    }
  }

  public void fromQueryString(String queryString) {
    List<Field> fieldList = FieldUtils.getAllFieldsList(this.getClass());
    List<NameValuePair> nameValuePairs = URLEncodedUtils
        .parse(queryString, Charset.forName("UTF-8"));

    for (Field f : fieldList) {
      String fieldName = f.getName();

      for (NameValuePair p : nameValuePairs) {
        String paramName = p.getName();

        if (fieldName.equalsIgnoreCase(paramName)) {
          try {
            FieldUtils.writeField(f, this, p.getValue());
          } catch (IllegalAccessException e) {
            //DO Nothing
          }
          break;
        }
      }
    }
  }
}
