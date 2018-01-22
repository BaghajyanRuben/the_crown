package com.game.thecrown.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.thecrown.Constants;

/**
 * Created by ruben on 2/12/17.
 */

public class Floor extends Actor {

	private Texture floor;
	private Body body;
	private World world;
	private Fixture fixture;

	public Floor(World world, Texture floor, float x, float y, float width) {
		this.floor = floor;
		this.world = world;

		BodyDef def = new BodyDef();
		def.position.set(x + width / 2, y - 0.5f);
		body = world.createBody(def);

		PolygonShape box = new PolygonShape();
		box.setAsBox(width / 2, 0.5f);
		fixture = body.createFixture(box, 1);
		fixture.setUserData(Constants.FLOOR_USER_DATA);
		box.dispose();

		setSize(width * Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
		setPosition((x - width / 2) * Constants.PIXELS_IN_METER, (y - 1) * Constants.PIXELS_IN_METER);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
//		batch.draw(floor, getX(), getY(), getWidth(), getHeight());
	}


	public void detach() {
		floor.dispose();
		body.destroyFixture(fixture);
		world.destroyBody(body);
	}
}
