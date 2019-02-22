package sg.dex.starfish.impl.memory;

import java.util.concurrent.CompletableFuture;

import sg.dex.starfish.AOperation;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

/**
 * Abstract base class for operations executed in-memory
 * @author Mike
 *
 */
public abstract class AMemoryOperation extends AOperation {

	/**
	 * Creates a memory opearation with the provided metadata.
	 * 
	 * @param meta The metadata string describing this operation
	 */
	protected AMemoryOperation(String meta) {
		super(meta);
	}
	
	@Override
	public Job invoke(Asset... params) {
		// default implementation for an invoke job in memory, using a Future<Asset>.
		// Implementations may override this for custom behaviour
		final CompletableFuture<Asset> future=new CompletableFuture<Asset>();
		
	    MemoryAgent.THREAD_POOL.submit(() -> {
	        try {
	        	Asset result=compute(params);
	        	future.complete(result); // success
	        } catch (Throwable t) {
	        	future.completeExceptionally(t); // failure
	        }
	        assert(future.isDone());
	    });		
	    
		return MemoryJob.create(future);
	}
	
	/**
	 * Computes the result of the invoke job using the provided assets
	 * 
	 * @param params The positional parameters for this computation
	 * @return Asset The result of the computation as an asset
	 */
	protected abstract Asset compute(Asset... params);
}