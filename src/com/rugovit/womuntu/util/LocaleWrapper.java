package com.rugovit.womuntu.util;

import java.util.Locale;

public class LocaleWrapper implements Comparable<LocaleWrapper> {
    private Locale locale;

    public LocaleWrapper(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    @Override
    public int compareTo(LocaleWrapper o) {
        return (locale.getLanguage()+locale.getCountry()).compareTo(o.getLocale().getLanguage()+o.getLocale().getCountry());
    }
}
