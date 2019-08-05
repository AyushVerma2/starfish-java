package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.oceanprotocol.squid.models.DDO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * To run the testcase you need to take latest pull of
 * https://github.com/oceanprotocol/barge
 * cd barge
 * ./start_ocean.sh
 */
@RunWith(JUnit4.class)
//@Ignore
public class SquidAssetTests {
    private Ocean ocean;
    private SquidAgent squidAgent;
    private SquidAsset squidAsset;

    @Before
    public void setup() {

        ocean = SquidHelperTest.getOcean();
        // create random DID
        DID did = DID.createRandom();

        // initialize squidAgent
        squidAgent = SquidAgent.create(SquidHelperTest.getPropertiesMap(), ocean, did);
    }


    @Test
    public void testCreateSquidAsset() throws URISyntaxException {

        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);
        // register created asset
        // any sf asset
        SquidAsset squidAsset_1 = squidAgent.registerAsset(asset);
        assertNotNull(squidAsset_1);
        assertNotNull(squidAsset_1.getAssetDID());
        assertNotNull(squidAsset_1.getOcean());
        assertEquals(squidAsset_1.isDataAsset(), true);
        assertEquals(asset.getAssetID(), squidAsset_1.getAssetID());



    }

    @Test
    public void testQueryDDOSquidAsset() {
        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);

        // register the Starfish asset created above into OCN
        squidAsset = squidAgent.registerAsset(asset);

        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetDID());


        // verifying the asset ddo , not it will not be null
        assertNotNull(squidAsset_1.getSquidDDO());
        // look up did , form squid agent ...

        // search asset by DDO and verify respective data
        assertEquals(squidAsset_1.getSquidDDO().metadata.base.name, Constant.DEFAULT_NAME);
        assertEquals(squidAsset_1.getSquidDDO().metadata.base.license, Constant.DEFAULT_LICENSE);
        assertTrue(squidAsset_1.getSquidDDO().metadata.base.price.equals("5"));

    }

    @Test
    public void testRequestTokens() throws EthereumException {

        BigInteger token = ocean.requestTokens(BigInteger.valueOf(10));
        assertEquals(BigInteger.valueOf(10), token);

    }

    @Test
    public void purchaseAndBalanceAsset() throws EthereumException {

        String receiverAddress = "0x064789569D09b4d40b54383d84A25A840E5D67aD";
        String receiverPasswd = "ocean_secret";

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);


        //eg: 5.9E-17
        Balance balanceBefore = ocean.getBalance(receiverAccount);

        Assert.assertNotNull(balanceBefore);

        // getting the price from asset metadata
        //eg: 10
        BigInteger assetPrice = BigInteger.valueOf(1);

        ocean.transfer(receiverAddress, assetPrice);

        //eg:  //eg: 6.9E-17
        Balance balanceAfter = ocean.getBalance(receiverAccount);

        assertEquals(1, balanceBefore.getEth().compareTo(balanceAfter.getEth()));


    }
    @Test
    public void testRegisterOnSurferAndChain() throws IOException, EthereumException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/SJR8961K_metadata.json")));

        // create asset using metadata and given content
        Asset memory_asset= ResourceAsset.create(asset_metaData,"assets/SJR8961K_content.json");

        // create surfer agent
        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        //register and upload the asset to surfer
        surfer.uploadAsset(memory_asset);

        // register the asset on Ocean Network
        SquidAsset squidAsset = squidAgent.registerAsset(memory_asset);


        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_FromChain = squidAgent.getAsset(squidAsset.getAssetDID());


        // verifying registration
        RemoteAsset aRemoteAsset =(RemoteAsset)surfer.getAsset(memory_asset.getAssetID());


       // validating name
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.name,memory_asset.getMetadata().get("name"));
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.name,aRemoteAsset.getMetadata().get("name"));

        // validating author
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.author,memory_asset.getMetadata().get("author"));
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.author,aRemoteAsset.getMetadata().get("author"));


        // validating content
        assertEquals(aRemoteAsset.getContent().length ,memory_asset.getContent().length);


        //*****Consume Asset ************

        // get the consumer account details
//        String receiverAddress = SquidHelperTest.getPropertiesMap().get("receiver.address");
//        String receiverPasswd = SquidHelperTest.getPropertiesMap().get("receiver.password");
//
//        Account consumerAccount = new Account(receiverAddress, receiverPasswd);
//
//        Balance balanceBefore = ocean.getBalance(consumerAccount);
//
//        // get price of asset
//        String price =squidAsset_FromChain.getSquidDDO().metadata.base.price ;
//        BigInteger priceOfAsset=new BigInteger(price);
//
//        // validate price
//        if(-1== priceOfAsset.compareTo(balanceBefore.getEth())){
//            ocean.transfer(receiverAddress, priceOfAsset);
//            String content=Utils.stringFromStream(aRemoteAsset.getContentStream());
//            assertNotNull(content);
//
//        }

    }


    @Test
    public void query() throws Exception {


        Map<String, Object> params = new HashMap<>();
        // this is the default value for license
        params.put("license", "CC-BY");

        List<DDO> results = ocean.getAssetsAPI().query(params).getResults();
        Assert.assertNotNull(results);

    }

    @Test
    public void testGetTransaction() throws URISyntaxException {

        String url =SquidHelperTest.getPropertiesMap().get("submarine.url");
        String account =SquidHelperTest.getPropertiesMap().get("test.account");

        Map<String,Object> transactionMap =ocean.getTransaction(url,account);

        assertNotNull(transactionMap);
        assertNotNull(transactionMap.get("result"));

        Object data=transactionMap.get("result");
        ArrayList<LinkedHashMap<String,String>> transactionlst =(ArrayList<LinkedHashMap<String,String>>)data;

        assertNotNull(transactionlst);
        assertNotNull(transactionlst.get(0));
        assertNotNull(transactionlst.get(0).get("hash"));


    }


}
