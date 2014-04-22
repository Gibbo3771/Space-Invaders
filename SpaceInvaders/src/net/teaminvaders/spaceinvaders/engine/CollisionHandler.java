/**
 * Copyright 2014 Stephen Gibson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.teaminvaders.spaceinvaders.engine;

import net.teaminvaders.spaceinvaders.entity.Entity;
import net.teaminvaders.spaceinvaders.entity.Projectile;

/**
 * A class with a few helper methods to check for collisions between an
 * {@link Entity} and a {@link Projectile}
 * 
 * @author Stephen Gibson
 */
public class CollisionHandler {

	/**
	 * Check if an entity and a projectile collided
	 * 
	 * @param entityA
	 * @param entityB
	 * @return
	 */
	public static boolean didCollide(Entity entityA, Entity entityB) {
		if (entityA == null || entityB == null)
			return false;
		if (entityA.getBounds().overlaps(entityB.getBounds()))
			return true;
		return false;
	}

}
