package com.msgkatz.ratesapp.data.repo.datastore.holders;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.data.net.rest.BinanceHtmlApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.data.repo.datastore.holders.base.IHolderBase;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by msgkatz on 28/08/2018.
 */

public class AssetHolder implements IHolderBase<Map<String, Asset>> {

    private final Context appContext;
    private final BinanceHtmlApi htmlApi;
    private final InnerModel innerModel;
    /** Map of Asset.ShortName, Asset **/
    private Map<String, Asset> data;


    public AssetHolder(Context appContext, BinanceHtmlApi htmlApi, InnerModel innerModel)
    {
        this.appContext = appContext;
        this.htmlApi = htmlApi;
        this.innerModel = innerModel;
    }

    @Override
    public Flowable<Optional<Map<String, Asset>>> getData() {
        if (data == null) {
            return htmlApi.getAssets().onExceptionResumeNext(Flowable.just(Response.error(404, ResponseBody.create(MediaType.parse("text/plain"), "err"))))
                    .map(new Function<Response<List<Asset>>, Optional<Map<String, Asset>>>() {
                        @Override
                        public Optional<Map<String, Asset>> apply(Response<List<Asset>> listResponse) throws Exception {

                            List<Asset> assetList; // = new ArrayList<>();

                            if (listResponse.isSuccessful())
                            {
                                assetList = listResponse.body();
                            } else {
                                assetList = loadAssetNamesFromAppAssets();
                            }

                            if (assetList != null && assetList.size() > 0)
                            {
                                //TODO: needs some sync
                                Map<String, Asset> tmp = new HashMap<>();
                                for (Asset item : assetList)
                                {
                                    tmp.put(item.getNameShort(), item);
                                }

                                data = tmp;
                                if (data != null)
                                    innerModel.setAssetMap(data);
                                return new Optional<>(data);
                            }
                            else {
                                return new Optional<>(null);
                            }
                        }
                    });
        }
        else
        {
            return Flowable.just(new Optional<Map<String, Asset>>(data));
        }
    }

    @Override
    public void flush() {
        this.data = null;
        innerModel.setAssetMap(null);
    }

    private List<Asset> loadAssetNamesFromAppAssets()
    {
        List<Asset> assetList = null;

        try {
            String json = null;
            InputStream is = this.appContext.getAssets().open("asset_names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            Type collectionType = new TypeToken<List<Asset>>(){}.getType();
            assetList = new Gson().fromJson(json, collectionType);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return assetList;

    }

}
