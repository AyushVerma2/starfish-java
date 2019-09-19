package sg.dex.starfish.integration.developerTC;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteBundle;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * As a developer working with asset bundles, I need a way to get a sub-asset
 */
public class AccessBundleAsset_16 {

    private RemoteAgent remoteAgent = AgentService.getRemoteAgent();

    private Map<String, Asset> getAssetMap() {
        // creating assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        // assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        return assetBundle;
    }

    /**
     * Create Remote bundle of 2 sub-asset
     */
    @Test
    public void testBundleCreation() {

        Map<String, Asset> assetBundle = getAssetMap();

        // create asset bundle without any custom metadata // so passing null
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, assetBundle, null);

        // register the bundle
        RemoteBundle aRemoteAsset = remoteAgent.registerAsset(remoteBundle);

        // getting the created asset bundle metadata
        Map<String, Object> metadata = aRemoteAsset.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle through API
        Map<String, Asset> allAssetMap = aRemoteAsset.getAll();

        assertEquals(allAssetMap.get("two").getAssetID(), assetBundle.get("two").getAssetID());
        assertEquals(allAssetMap.get("one").getAssetID(), assetBundle.get("one").getAssetID());

    }

    /**
     * Create Remote bundle of 2 sub-asset
     */
    @Test
    public void testBundleCreationWithAdditionalMetadata() {

        Map<String, Asset> assetBundle = getAssetMap();

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("name", "testBundle");
        metaMap.put("author", "dex-starfish");

        // create asset bundle without any custom metadata // so passing null
        Bundle remoteBundle = RemoteBundle.create(remoteAgent, assetBundle, metaMap);

        // register the bundle
        Bundle aRemoteAsset = remoteAgent.registerAsset(remoteBundle);

        // getting the created asset bundle metadata
        Map<String, Object> metadata = aRemoteAsset.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), "testBundle");
        assertEquals(metadata.get("author"), "dex-starfish");

        // getting the contents of asset bundle through API
        Map<String, Asset> allAssetMap = aRemoteAsset.getAll();

        assertEquals((allAssetMap.get("two").toString()), assetBundle.get("two").getAssetID());
        assertEquals((allAssetMap.get("one").toString()), assetBundle.get("one").getAssetID());

    }

    /**
     * Create Empty bundle
     */
    @Test
    public void testCreateEmptyBundle() {
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, null);
        // register the bundle
        RemoteBundle aRemoteAsset = remoteAgent.registerAsset(remoteBundle);
        assertTrue(aRemoteAsset.getAll().isEmpty());
    }

    /**
     * Create Empty bundle and then add sub-asset
     */
    @Test
    public void testCreateEmptyBundleThenAddSubAsset() {
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, null);
        RemoteBundle aRemoteAsset = remoteAgent.registerAsset(remoteBundle);
        assertTrue(aRemoteAsset.getAll().isEmpty());

        Bundle remoteBundleWithSubAsset = remoteBundle.addAll(getAssetMap());
        RemoteBundle aRemoteAsset1 = remoteAgent.registerAsset(remoteBundleWithSubAsset);

        assertEquals(aRemoteAsset1.getAll().size(), 2);
        // old bundle will remain same
        assertTrue(aRemoteAsset.getAll().isEmpty());
    }

    /**
     * Test Nested Bundle
     */
    @Test
    public void testNestedBundle() {
        Map<String, Asset> nestedAsset = getAssetMap();

        // create asset bundle without any custom metadata // so passing null
        RemoteBundle nestedBundle = RemoteBundle.create(remoteAgent, nestedAsset);

        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("nested", nestedBundle);

        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, assetBundle);

        RemoteBundle nestedB = remoteBundle.get("nested");
        assertEquals(nestedB.getAll().size(), 2);

    }

    @Test(expected = StarfishValidationException.class)
    public void testNullAgent() {

        RemoteBundle remoteBundle = RemoteBundle.create(null, null);

    }

}
