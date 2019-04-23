package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AListing;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for creating the listing instance.
 * To create and instance user just need to pass the agent and the data(metadata)
 * for which the listing instance should be created.
 * It also sever getting the meta data of existing listing ,updating the existing listing.
 */

public class RemoteListing extends AListing {

    // local map to cache the listing data
    private static Map<String, Object> metaDataCache = null;
    // remote agent reference
    private RemoteAgent remoteAgent;
    // listing id
    private String listing_id;


    /**
     * To get the reference of existing listing user need to pass the remote Agent and the existing listing id.
     *
     * @param remoteAgent
     * @param id
     */
    private RemoteListing(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.listing_id = id;
    }

    /**
     * To get the Reference of Existing Listing
     *
     * @param agent
     * @param id
     * @return
     */
    public static RemoteListing create(RemoteAgent agent, String id) {
        RemoteListing remoteListing = new RemoteListing(agent, id);
        initializeCache();
        return remoteListing;
    }

    /**
     * API to create the local cache instance
     */
    private static void initializeCache() {
        if (null == metaDataCache) {
            metaDataCache = new HashMap<>();
        }

    }

    @Override
    public Asset getAsset() {

        return remoteAgent.getAsset(getAssetId());
    }

    @Override
    public Object getAgreement() {
        return getAggrement();
    }

    @Override
    public Asset purchase(Account account) {
        throw new TODOException();
    }

    @Override
    public Listing refresh() {
        metaDataCache.put(listing_id, remoteAgent.getListingMetaData(listing_id));
        return this;
    }


    @Override
    public Map<String, Object> getMetaData() {
        Map<String, Object> metaData = metaDataCache.get(listing_id) == null ?
                remoteAgent.getListingMetaData(listing_id) : (Map<String, Object>) metaDataCache.get(listing_id);
        return metaData;

    }

    @Override
    public Map<String, Object> getInfo() {
        return (Map<String, Object>) getMetaData().get("info");
    }

    private String getAssetId() {
        return getMetaData().get("assetid").toString();
    }

    private String getUserId() {
        return getMetaData().get("userid").toString();
    }

    private String getAggrement() {
        return getMetaData().get("agreement").toString();
    }

    private String getListing_id() {
        return getMetaData().get("id").toString();
    }

}