package developerTC;

import org.junit.jupiter.api.*;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * As a developer wishing to make an asset available for download,
 * I need a way to upload the asset data to an appropriate service provider
 */
public class TestUploadAsset_1_IT {


    private RemoteAgent remoteAgent;

    @BeforeAll
    @DisplayName("Check if RemoteAgent is up!!")
    public static void init() {
        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
    }

    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
    }


    @Test
    public void testUploadAssetWithMetaData() throws IOException {
// read metadata
        String metadata = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_metadata.json")));

        byte[] data = {2, 3, 4, 5, 6, 7, 8, 9, 0};
        Asset a = MemoryAsset.create(data, metadata);
        RemoteDataAsset remoteAssetUpload = remoteAgent.uploadAsset(a);

        assertEquals(remoteAssetUpload.getContent().length, data.length);
        assertEquals(a.getAssetID(), remoteAssetUpload.getAssetID());
        assertArrayEquals(data, remoteAssetUpload.getContent());

    }

    @Test
    public void testNullUpload() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            remoteAgent.uploadAsset(null);
        });

    }

    @Test
    public void testAssetIdAfterRegistrationAndUpload() {
        byte[] data = {2, 3, 4, 5, 6, 7, 8, 9, 0};
        MemoryAsset memoryAsset = MemoryAsset.create(data);
        ARemoteAsset aRemoteAsset = remoteAgent.registerAsset(memoryAsset);
        ARemoteAsset aRemoteAsset1 = remoteAgent.uploadAsset(memoryAsset);
        assertArrayEquals(aRemoteAsset.getContent(), data);
        assertEquals(aRemoteAsset.getAssetID(), memoryAsset.getAssetID());
        assertEquals(aRemoteAsset1.getAssetID(), memoryAsset.getAssetID());
        assertEquals(aRemoteAsset1.getAssetID(), aRemoteAsset.getAssetID());

    }


}


