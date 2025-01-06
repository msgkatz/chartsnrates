package com.msgkatz.ratesapp.data.entities.rest;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msgkatz.ratesapp.utils.Parameters;

import javax.annotation.Generated;

/**
 * Assets are BTC, ETH, etc.
 *
 * Created by msgkatz on 22/07/2018.
 */

@Generated("org.jsonschema2pojo")
public class AssetDT implements Comparable<AssetDT> {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("assetCode")
    @Expose
    private String nameShort;

    @SerializedName("assetName")
    @Expose
    private String nameLong;

    @SerializedName("logoUrl")
    @Expose
    private String logoShortUrl;

    private String logoFullUrl;

    public AssetDT(int id, String nameShort, String nameLong)
    {
        this.id = id;
        this.nameShort = nameShort;
        this.nameLong = nameLong;
    }

    public int getId() {
        return id;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getNameLong() {
        return nameLong;
    }

    public String getLogoFullUrl() {
        if (logoFullUrl == null)
        {
            logoFullUrl = (this.logoShortUrl != null && this.logoShortUrl.contains("http"))
                    ? this.logoShortUrl
                    : String.format("%1$s%2$s", Parameters.BASE_URL, this.logoShortUrl);

            if (logoFullUrl != null && logoFullUrl.contains(Parameters.BASE_URL_TO_CHANGE))
            {
                logoFullUrl = logoFullUrl.replace(Parameters.BASE_URL_TO_CHANGE, Parameters.BASE_URL_TO_CHANGE_TO);
            }
        }
        return logoFullUrl;
    }

    @Override
    public String toString() {
        return String.format("id:%1$s, nameShort:%2$s, nameLong:%3$s, logoFullUrl:%4$s",
                Integer.toString(id), nameShort, nameLong, getLogoFullUrl());
    }

    @Override
    public int compareTo(@NonNull AssetDT o) {
        return this.getNameShort().compareTo(o.getNameShort());
    }

    @Override
    public int hashCode() {
        return this.getNameShort().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof AssetDT)) return false;

        AssetDT o = (AssetDT) obj;

        return this.getNameShort()
                .equals(o.getNameShort());
    }
}
