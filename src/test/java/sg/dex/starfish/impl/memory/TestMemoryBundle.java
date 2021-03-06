package sg.dex.starfish.impl.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.DID;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("javadoc")
public class TestMemoryBundle {

    private Asset a1;
    private Asset a2;
    private Asset a3;
    private Asset a4;
    private Bundle b1;

    @BeforeEach
    public void setup() {
        a1 = MemoryAsset.create(new byte[]{1, 3, 4});
        a2 = MemoryAsset.create(new byte[]{2, 3, 4});
        a3 = MemoryAsset.create(new byte[]{3, 3, 4});
        a4 = MemoryAsset.create(new byte[]{4, 3, 4});
        b1 = createTestBundle();
    }

    private Bundle createTestBundle() {

        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        assetBundle.put("three", a3);
        assetBundle.put("four", a4);

        // creating additional metadata
        Map<String, Object> additionalMetaData = new HashMap<>();
        additionalMetaData.put("name", "Bundle1-Test");

        // create asset bundle with default metadata and default MemoryAgent
        Bundle memoryAssetBundle = MemoryBundle.create(assetBundle, additionalMetaData);
        DID bundleDid = memoryAssetBundle.getDID();
        assertTrue(bundleDid.toString().contains(memoryAssetBundle.getAssetID()));
        assertTrue(bundleDid.toString().contains(MemoryAgent.create().getDID().toString()));
        return memoryAssetBundle;
    }

    @Test
    public void testAttrributes() {
        assertTrue(b1.isBundle());
        assertFalse(b1.isDataAsset());
        assertFalse(b1.isOperation());
    }

    /**
     * Test bundle creation with bundle name, so name will be null
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBundleMetadata() {
        // getting the created asset bundle metadata
        Map<String, Object> metadata = b1.getMetadata();

        // getting the contents of asset bundle through metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


        // getting the contents of asset bundle through API
        Map<String, Asset> allAssetMap = b1.getAll();
        assertEquals(1, allAssetMap.get("one").getContent()[0]);
        assertEquals(3, allAssetMap.get("three").getContent()[0]);
        assertEquals(4, allAssetMap.get("four").getContent()[0]);
    }

    /**
     * Test bundle creation with name and custom meta data
     */
    @Test
    public void testCreationWithBundleName() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);


        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);


        //Custom metadata
        Map<String, Object> customData = new HashMap<>();
        customData.put("name", "My First Bundle");

        // create asset bundle without any custom metadata // so passing null
        Bundle memoryAssetBundle = MemoryBundle.create(assetBundle, customData);

        // getting the ceated asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        assertEquals(metadata.get("name"), "My First Bundle");

    }

    /**
     * Add sub-Asset map in empty bundle
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddAssetMapInEmptyBundle() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);


        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);

        // create asset bundle without any custom metadata // so passing null
        // if assetMap is passed as null it will create an empty map for that
        Bundle bundle = MemoryBundle.create(assetBundle, null);

        byte[] data3 = {4, 16, 7};
        Asset a3 = MemoryAsset.create(data3);

        byte[] data4 = {15, 16, 17};
        Asset a4 = MemoryAsset.create(data4);

        assetBundle.put("three", a3);
        assetBundle.put("four", a4);
        // as bundle is immutable ,so it will create a new bundle with existing sub-asset and the new sub-asset
        Bundle newBundle = bundle.addAll(assetBundle);


        // getting the created asset bundle metadata
        Map<String, Object> metadata = newBundle.getMetadata();


        // getting the contents of asset bundle from metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());

        // getting the contents of asset bundle through API
        Map<String, Asset> allAssetMap = newBundle.getAll();

        assertEquals(allAssetMap.get("one").getAssetID(), a1.getAssetID());
        assertEquals(allAssetMap.get("two").getAssetID(), a2.getAssetID());
    }


    /**
     * APi to test get sub-asset bundle by name
     */
    @Test
    public void getSubAssetByName() {
        Asset subAsset = MemoryAsset.createFromString("Test bundle asset by sub asset name");
        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", subAsset);

        Bundle bundle = MemoryBundle.create(assetBundle);

        assertEquals(bundle.getAll().size(), assetBundle.size());
        assertEquals(bundle.isBundle(), true);
        assertEquals(bundle.getMetadata().get(Constant.TYPE), Constant.BUNDLE);
        assertEquals(bundle.get("one").getAssetID(), subAsset.getAssetID());

        // TODO: Figure out correct behaviour when registering a bundle
        // Agent memoryAgent = MemoryAgent.create();
        // memoryAgent.registerAsset(bundle);
        // assertEquals(subAsset,memoryAgent.getAsset(subAsset.getAssetID()));
    }

    @Test
    public void testEmptyBundleCreation() {


        Bundle bundle = MemoryBundle.create(null);
        assertTrue(bundle.getAll().isEmpty());
        assertEquals(bundle.isBundle(), true);
        assertEquals(bundle.getMetadata().get(Constant.TYPE), Constant.BUNDLE);

    }


    @Test
    public void testAddAllInExistingBundle() {
        Asset subAsset1 = MemoryAsset.createFromString("Sub Asset 1");
        Asset subAsset2 = MemoryAsset.createFromString("Sub Asset 2");
        Asset subAsset3 = MemoryAsset.createFromString("Sub Asset 3");
        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("1", subAsset1);
        assetBundle.put("2", subAsset2);
        assetBundle.put("3", subAsset3);

        Bundle bundle = MemoryBundle.create(null);
        bundle = bundle.addAll(assetBundle);
        assertEquals(subAsset1.getAssetID(), bundle.get("1").getAssetID());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNestedBundle() {

        Asset subAsset = MemoryAsset.createFromString("Test bundle asset by sub asset name");
        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", subAsset);

        Bundle nestedBundle = MemoryBundle.create(assetBundle);

        Asset subAsset2 = MemoryAsset.createFromString("Test bundle Main");
        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle2 = new HashMap<>();
        assetBundle2.put("1", subAsset2);
        assetBundle2.put("2", nestedBundle);

        Bundle mainBundle = MemoryBundle.create(assetBundle2);
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) mainBundle.getMetadata().get("contents");
        assertEquals(contents.get("2").get("assetID"), nestedBundle.getAssetID());
        assertEquals(mainBundle.get("2").isBundle(), true);
    }

}
