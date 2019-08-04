package com.rugovit.womuntu.model.csv_mapping;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvBadConverterException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MappingByNameAndPosition<T> extends HeaderColumnNameMappingStrategy<T> {
    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        this.fieldMap = new FieldMapByName(this.errorLocale);
        Iterator i$ = loadFields(this.getType()).iterator();

        while(i$.hasNext()) {
            Field field = (Field)i$.next();
            boolean required;
            String columnName;
            if (field.isAnnotationPresent(CsvCustomBindByName.class)) {
                CsvCustomBindByName annotation = field.getAnnotation(CsvCustomBindByName.class);
                columnName = annotation.column().toUpperCase().trim();
                if (StringUtils.isEmpty(columnName)) {
                    columnName = field.getName().toUpperCase();
                }

                Class<? extends AbstractBeanField> converter = ((CsvCustomBindByName)field.getAnnotation(CsvCustomBindByName.class)).converter();
                BeanField<T> bean = this.instantiateCustomConverter(converter);
                bean.setField(field);
                required = annotation.required();
                bean.setRequired(required);
                this.fieldMap.put(columnName, bean);
            } else {
                String locale;
                Class mapType;
                String columnRegex;
                if (field.isAnnotationPresent(CsvBindAndSplitByName.class)) {
                    CsvBindAndSplitByName annotation = field.getAnnotation(CsvBindAndSplitByName.class);
                    required = annotation.required();
                    columnName = annotation.column().toUpperCase().trim();
                    locale = annotation.locale();
                    columnRegex = annotation.splitOn();
                    String writeDelimiter = annotation.writeDelimiter();
                    mapType = annotation.collectionType();
                    Class<?> elementType = annotation.elementType();
                    CsvConverter converter = this.determineConverter(field, elementType, locale);
                    if (StringUtils.isEmpty(columnName)) {
                        this.fieldMap.put(field.getName().toUpperCase(), new BeanFieldSplit(field, required, this.errorLocale, converter, columnRegex, writeDelimiter, mapType));
                    } else {
                        this.fieldMap.put(columnName, new BeanFieldSplit(field, required, this.errorLocale, converter, columnRegex, writeDelimiter, mapType));
                    }
                } else if (field.isAnnotationPresent(CsvBindAndJoinByName.class)) {
                    CsvBindAndJoinByName annotation = field.getAnnotation(CsvBindAndJoinByName.class);
                    required = annotation.required();
                    columnRegex = annotation.column();
                    locale = annotation.locale();
                    Class<?> elementType = annotation.elementType();
                    mapType = annotation.mapType();
                    CsvConverter converter = this.determineConverter(field, elementType, locale);
                    if (StringUtils.isEmpty(columnRegex)) {
                        this.fieldMap.putComplex(field.getName(), new BeanFieldJoinStringIndex(field, required, this.errorLocale, converter, mapType));
                    } else {
                        this.fieldMap.putComplex(columnRegex, new BeanFieldJoinStringIndex(field, required, this.errorLocale, converter, mapType));
                    }
                } else {
                    CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                    required = annotation.required();
                    columnName = annotation.column().toUpperCase().trim();
                    locale = annotation.locale();
                    CsvConverter converter = this.determineConverter(field, field.getType(), locale);
                    if (StringUtils.isEmpty(columnName)) {
                        this.fieldMap.put(field.getName().toUpperCase(), new BeanFieldSingleValue(field, required, this.errorLocale, converter));
                    } else {
                        this.fieldMap.put(columnName, new BeanFieldSingleValue(field, required, this.errorLocale, converter));
                    }
                }
            }
        }

    }
    private List<Field> loadFields(Class<? extends T> cls) {
        List<Field> fields = new LinkedList();
        Field[] arr$ = FieldUtils.getAllFields(cls);
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            if (field.isAnnotationPresent(CsvBindByName.class) || field.isAnnotationPresent(CsvCustomBindByName.class) || field.isAnnotationPresent(CsvBindAndSplitByName.class) || field.isAnnotationPresent(CsvBindAndJoinByName.class)) {
               if(field.isAnnotationPresent(CsvBindByPosition.class) ){
                   CsvBindByPosition ta = field.getAnnotation(CsvBindByPosition.class);
                   fields.add(ta.position(),field);
               }
               else fields.add(field);
            }
        }

        this.annotationDriven = !fields.isEmpty();
        return fields;
    }

}
