package services

import models.Protein

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.IMap


/**
 * Service for global caching. By default this service should be singleton.
 * 
 * Now it works in very straight way: There are two maps. One for proteins
 * with very straight structure: protein_id -> Protein object. And another is
 * with list of interactors. Structure: protein_id -> List.
 * 
 * 
 * @author den.konakov@gmail.com
 */
@Component
class GlobalCachingService {
	
	// This is stupid, I know. 
	// TODO: Add Spring Context to UnitTests and remove this public constructor ASAP
	private HazelcastInstance instance
	public GlobalCachingService() {
		instance = Hazelcast.newHazelcastInstance(null)
	}
	
	private static final Log log = LogFactory.getLog(GlobalCachingService.class)
	
	public static enum Caches {
		PROTEINS("proteins"),
		INTERACT("interactors")
		
		private name
		private Caches(String name) {
			this.name = name
		}
		
		public String toString() {
			return name
		}
	}
	
	public IMap getProteinsMap() {
		return map(Caches.PROTEINS)
	}
	
	public IMap getInteractMap() {
		return map(Caches.INTERACT)
	}
	
	public fillSafeMap(Caches mapToFill, String proteinId, value) {
		log.info("--fillSafeMap:Enter--")
		
		def mp = map(mapToFill)
		def lock = instance.getLock(mp)
		
		// Try lock this map, 'cause other processes can locked it already
		if(lock.tryLock()) {
			log.info("fillSafeMap:Lock acquired for updating")
			
			// For consistency, do all updates for this cache in distributed transaction
			def txn = instance.getTransaction()
			txn.begin()
			
			try {
				log.info("fillSafeMap:Updating in txn:"+txn)
				// Update the results
				mp.put(proteinId, value)
				
				txn.commit()
				log.info("fillSafeMap:Transaction commited")
			} catch (Throwable t) {
				t.printStackTrace()
				txn.rollback()
				log.info("fillSafeMap:Transaction rollback")
			} finally {
				lock.unlock()
				log.info("fillSafeMap:Lock released")
			}
		} else {
			log.info("fillSafeMap:Another process is updating MAP:"+mp)
		}
		
		log.info("--fillSafeMap:Leave--")
	}
	
	private IMap map(Caches map) {
		instance.getMap(map.toString())
	}
}