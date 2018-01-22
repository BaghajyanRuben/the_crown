package com.game.thecrown.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by ruben on 2/11/17.
 */

public class Rock extends Actor {

	private World world;
	private Body rockBody;
	private Fixture rockFixture;
	private Texture rockTexture;
	private TextureRegion rockTextureRegion;
	private Vector2 position;

	public Rock(World world){
		this.world = world;
		position = new Vector2(100, 2);

		rockTexture = new Texture(Gdx.files.internal("rock.png"));

		rockBody = world.createBody(createRockBodyDef());

		rockFixture = createRockFixture(rockBody);

		rockFixture.setUserData("rock");

		setSize(rockTexture.getWidth(), rockTexture.getHeight());

	}


	public void detach(){
		rockBody.destroyFixture(rockFixture);
		world.destroyBody(rockBody);
	}

	private BodyDef createRockBodyDef() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(position);
		return def;
	}

	private Fixture createRockFixture(Body body){
		PolygonShape box = new PolygonShape();
		box.setAsBox(rockTexture.getWidth()/2, rockTexture.getHeight()/2);
		Fixture rockFixture = body.createFixture(box,1);
		box.dispose();
		return rockFixture;
	}

	public Fixture getRockFixture() {
		return rockFixture;
	}

	public Body getRockBody() {
		return rockBody;
	}

	@Override
	public void act(float delta) {
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(rockTexture, getX(), getY());
	}

}
