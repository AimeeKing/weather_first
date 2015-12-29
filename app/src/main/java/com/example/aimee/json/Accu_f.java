package com.example.aimee.json;

/**
 * Created by Aimee on 2015/12/26.
 */
public class Accu_f {


    public String getPrecipitationProbability() {
        return PrecipitationProbability;
    }

    public void setPrecipitationProbability(String precipitationProbability) {
        PrecipitationProbability = precipitationProbability;
    }

    public String getEpochDate() {
        return EpochDate;
    }

    public void setEpochDate(String epochDate) {
        EpochDate = epochDate;
    }

    public String getSun_EpochRise() {
        return Sun_EpochRise;
    }

    public void setSun_EpochRise(String sun_EpochRise) {
        Sun_EpochRise = sun_EpochRise;
    }

    public String getSun_EpochSet() {
        return Sun_EpochSet;
    }

    public void setSun_EpochSet(String sun_EpochSet) {
        Sun_EpochSet = sun_EpochSet;
    }

    String EpochDate,Sun_EpochRise,Sun_EpochSet,PrecipitationProbability;
}
