package sg.dex.starfish.impl.squid;

import com.oceanprotocol.common.helpers.EncodingHelper;
import com.oceanprotocol.common.web3.KeeperService;
import com.oceanprotocol.keeper.contracts.DIDRegistry;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.api.config.OceanConfigFactory;
import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class DexResolver implements Resolver {
    private DIDRegistry contract;
    private SquidService squidService;

    /**
     * Create DexResolver
     *
     * @param contract     contract
     * @param squidService squidService
     */
    private DexResolver(DIDRegistry contract, SquidService squidService) {
        this.contract = contract;
        this.squidService = squidService;
    }

    /**
     * Creates a DexResolver
     *
     * @param squidService:   SquidService with set Squid configuration
     * @param addressFrom:    owner of DID record in the ledger. Only this address is allowed to update it
     * @param password:       its password
     * @param credentialFile: its parity credential file
     * @return DexResolver The newly created DexResolver
     * @throws IOException, CipherException
     */
    public static DexResolver create(SquidService squidService, String addressFrom, String password, String credentialFile) throws IOException, CipherException {
        Properties properties = squidService.getProperties();
        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        oceanConfig.setMainAccountAddress(addressFrom);
        oceanConfig.setMainAccountPassword(password);
        oceanConfig.setMainAccountCredentialsFile(credentialFile);
        String address = (String) properties.getOrDefault("contract.DIDRegistry.address", "");
        KeeperService keeper = squidService.getKeeperService(oceanConfig);
        DIDRegistry contract = DIDRegistry.load(address, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        return new DexResolver(contract, squidService);
    }

    /**
     * Creates default DexResolver
     *
     * @return DexResolver The newly created DexResolver
     * @throws IOException
     */
    public static Resolver create() throws IOException {
        // todo need to remote as create is failing now may be due to Nile network is issue
        return new LocalResolverImpl();
//         return create("resolver_default.properties");
    }

    /**
     * Creates a DexResolver
     *
     * @param configFile configFile. All information about account credentials will be taken from this file
     * @return DexResolver The newly created DexResolver
     * @throws IOException
     */
    public static DexResolver create(String configFile) throws IOException {
        SquidService squidService = SquidService.create(configFile);
        Properties properties = squidService.getProperties();
        String address = (String) properties.getOrDefault("contract.DIDRegistry.address", "");
        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        KeeperService keeper = null;
        try {
            keeper = squidService.getKeeperService(oceanConfig);
        } catch (CipherException e) {
            System.err.println("Wrong credential file or its password");
            e.printStackTrace();
        }
        DIDRegistry contract = DIDRegistry.load(address, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        return new DexResolver(contract, squidService);
    }

    @Override
    public String getDDOString(DID did) throws ResolverException {
        String didHash = did.getID();
        BigInteger blockNumber = BigInteger.valueOf(0);
        try {
            blockNumber = contract.getBlockNumberUpdated(EncodingHelper.hexStringToBytes(didHash)).send();
        } catch (UnsupportedEncodingException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(blockNumber), DefaultBlockParameter.valueOf(blockNumber), contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIDRegistry.DIDATTRIBUTEREGISTERED_EVENT));
        String didTopic = "0x" + didHash;
        filter.addOptionalTopics(didTopic);

        Flowable<DIDRegistry.DIDAttributeRegisteredEventResponse> floable = contract.dIDAttributeRegisteredEventFlowable(filter);
        ArrayList<DIDRegistry.DIDAttributeRegisteredEventResponse> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            outcome.add(log);
        });

        return outcome.size() == 1 ? outcome.get(0)._value : null;
    }

    @Override
    public void registerDID(DID did, String ddo) throws ResolverException {
        String checksum = "0x0";

        TransactionReceipt receipt = null;
        try {
            receipt = contract.registerAttribute(
                    EncodingHelper.hexStringToBytes(did.getID()),
                    EncodingHelper.hexStringToBytes(Hex.toZeroPaddedHexNoPrefix(checksum)),
                    Arrays.asList(squidService.getProvider()), ddo).send();
        } catch (IOException e) {
            throw new ResolverException(e);
        } catch (CipherException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        if (!receipt.getStatus().equals("0x1"))
            throw new ResolverException();
    }
}