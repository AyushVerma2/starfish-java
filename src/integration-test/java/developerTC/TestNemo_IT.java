package developerTC;

import org.junit.jupiter.api.*;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;

public class TestNemo_IT {

    private RemoteAgent nemoAgent;

    //@BeforeAll
//    @DisplayName("Check if RemoteAgent is up!!")
//    public static void init() {
//        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
//    }


    @BeforeEach
    public void setup() {
        nemoAgent = AgentService.getNemoAgent();

    }

    @Disabled
    public void testRegister() {
        String data = "Simple memory Asset";
        Asset asset = MemoryAsset.createFromString(data);
        Asset remoteAsset = nemoAgent.registerAsset(asset);

        Assertions.assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        Assertions.assertTrue(remoteAsset.isDataAsset());
        Assertions.assertEquals(remoteAsset.getMetadataString(), asset.getMetadataString());
    }
}
