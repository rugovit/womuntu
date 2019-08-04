package com.rugovit.womuntu.model;


import javax.xml.bind.annotation.XmlAccessType;
        import javax.xml.bind.annotation.XmlAccessorType;
        import javax.xml.bind.annotation.XmlElement;
        import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "resources")
@XmlAccessorType (XmlAccessType.FIELD)
public class LocaleTranslations
{
    @XmlElement(name = "string")
    private List<com.rugovit.womuntu.model.LocaleTranslation> translations = null;

    public List<com.rugovit.womuntu.model.LocaleTranslation>getTranslations() {
        return translations;
    }

    public void setTranslations(List<com.rugovit.womuntu.model.LocaleTranslation> employees) {
        this.translations = employees;
    }
}