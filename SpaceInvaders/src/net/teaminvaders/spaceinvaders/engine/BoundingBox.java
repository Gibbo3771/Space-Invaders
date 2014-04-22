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

import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author Stephen Gibson
 */
public class BoundingBox extends Rectangle {

	private static final long serialVersionUID = 1L;

	/** The entity belonging to this bounding box */
	public Entity owner;

	public BoundingBox() {
		
	}

	public BoundingBox(Rectangle rect) {
		super(rect);
		
	}

	public BoundingBox(float x, float y, float width, float height) {
		super(x, y, width, height);
		
	}
	
	public Entity getOwner() {
		return owner;
	}
	
	public void setOwner(Entity owner) {
		this.owner = owner;
	}

}


