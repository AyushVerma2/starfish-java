package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.DexChainException;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory local resolver implementation.
 */
public class LocalResolverImpl implements Resolver {
    private static final Map<DID, String> ddoCache = new HashMap<>();
    private static Resolver nextInChain;

    public LocalResolverImpl(Resolver next) {
        nextInChain = next;
    }

    public LocalResolverImpl() {
        this(null);
    }

    @Override
    public String getDDOString(DID did) throws DexChainException {
        // remove path to get DDO for basic did
        did = did.withoutPath();
        String localDDO = ddoCache.get(did);
        if (localDDO != null) {
            return localDDO;
        }
        if (nextInChain == null) return null;

        return nextInChain.getDDOString(did);
    }

    @Override
    public void registerDID(DID did, String ddoString) throws DexChainException {
        installLocalDDO(did, ddoString);
    }

    /**
     * Registers a DID with a DDO on the local machine.
     * <p>
     * This registration is intended for testing purposes.
     *
     * @param did       A did to register
     * @param ddoString A string containing a valid DDO in JSON Format
     */
    private void installLocalDDO(DID did, String ddoString) {
        if (null == did || null == ddoString)
            throw new IllegalArgumentException("DID/DDO cannot be null");
        did = did.withoutPath();
        ddoCache.put(did, ddoString);
    }

    /**
     * Registers a DID with a DDO in the context of the local machine.
     * <p>
     * This registration is intended for testing purposes.
     *
     * @param did A did to register
     * @param ddo A Map containing a valid DDO
     */
    public void installLocalDDO(DID did, Map<String, Object> ddo) {
        installLocalDDO(did, JSON.toPrettyString(ddo));
    }

    /**
     * Gets a DDO for a specified DID via the Universal resolver
     *
     * @param did DID to resolve as a String
     * @return The DDO as a JSON map
     */
    public Map<String, Object> getDDO(String did) {
        return JSON.parse(getDDOString(did));
    }

    /**
     * Gets a DDO for a specified DID via the Universal resolver
     *
     * @param did DID to resolve as a String
     * @return The DDO as a JSON map
     */
    public String getDDOString(String did) {
        return getDDOString(DID.parse(did));
    }

}
