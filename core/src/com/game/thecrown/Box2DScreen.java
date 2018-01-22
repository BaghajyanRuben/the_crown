package com.game.thecrown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by ruben on 2/11/17.
 */

public class Box2DScreen extends BaseScreen implements SwipeDetector.SwipeListener {

	private World world;
	private Box2DDebugRenderer renderer;
	private OrthographicCamera camera;
	private Body playerBody;
	private Body floorBody;
	private Body rockBody;
	private Fixture playerFixture;
	private Fixture floorFixture;
	private Fixture rockFixture;
	boolean mustJump;
	boolean dropping;
	boolean isAlive = true;
	boolean justJump = false;

	public Box2DScreen(GameMain game) {
		super(game);
	}

	@Override
	public void show() {
		world = new World(new Vector2(0, -10), true);
		renderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(16, 9);
		camera.translate(0, 1);

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();


				if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")) ||
						(fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player"))){
					if (justJump){
						mustJump = true;
					}
					dropping = false;
				}

				if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("rock")) ||
						(fixtureA.getUserData().equals("rock") && fixtureB.getUserData().equals("player"))){
					isAlive = false;
				}

			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				if (fixtureA == playerFixture && fixtureB == floorFixture){
					dropping = true;
				}
				if (fixtureA == floorFixture && fixtureB == playerFixture){
					dropping = true;
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold manifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse contactImpulse) {

			}
		});

		playerBody = world.createBody(createDynamicBodyDef());
		floorBody = world.createBody(createFloorBodyDef());
		rockBody = world.createBody(createRockBodyDef(6f));

		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5f, 0.5f);
		playerFixture = playerBody.createFixture(box,3);
		box.dispose();

		PolygonShape floorBox = new PolygonShape();
		floorBox.setAsBox(500, 0.5f);
		floorFixture = floorBody.createFixture(floorBox,1);
		floorBox.dispose();

		rockFixture = createRockFixture(rockBody);

		playerFixture.setUserData("player");
		floorFixture.setUserData("floor");
		rockFixture.setUserData("rock");

		GestureDetector gestureDetector = new GestureDetector(new SwipeDetector(this));
		Gdx.input.setInputProcessor(gestureDetector);
	}

	private BodyDef createRockBodyDef(float x) {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, 0.5f);
		return def;
	}

	private Fixture createRockFixture(Body body){
		Vector2[] vector2s = new Vector2[3];
		vector2s[0] = new Vector2(-0.5f, -0.5f);
		vector2s[1] = new Vector2(0.5f, -0.5f);
		vector2s[2] = new Vector2(0, 0.5f);
		PolygonShape box = new PolygonShape();
		box.set(vector2s);
		Fixture rockFixture = body.createFixture(box,1);
		box.dispose();
		return rockFixture;
	}

	private BodyDef createDynamicBodyDef(){

		BodyDef def = new BodyDef();
		def.position.set(0.5f, 0.5f);
		def.type = BodyDef.BodyType.DynamicBody;
		return def;
	}

	private BodyDef createFloorBodyDef(){
		BodyDef def = new BodyDef();
		def.position.set(-1, -1);
		return def;
	}

	@Override
	public void dispose() {
		playerBody.destroyFixture(playerFixture);
		floorBody.destroyFixture(floorFixture);
		rockBody.destroyFixture(rockFixture);
		world.destroyBody(playerBody);
		world.destroyBody(floorBody);
		world.destroyBody(rockBody);
		world.dispose();
		renderer.dispose();
	}

	@Override
	public void render(float v) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (mustJump) {
			mustJump = false;
			jump();
		}

		if (justJump && !dropping){
			mustJump = true;
		}

		if (isAlive) {
			float velocityY = rockBody.getLinearVelocity().y;
			rockBody.setLinearVelocity(-8, velocityY);
		}

		world.step(v, 6, 2);
		camera.update();
		renderer.render(world, camera.combined);
	}

	private void jump(){
		Vector2 position = playerBody.getPosition();
		playerBody.applyLinearImpulse(0, 20, position.x, position.y, true);
		justJump = false;
	}

	@Override
	public void onSwipeUp(boolean isUp) {
		justJump = isUp;
	}

	@Override
	public void onSwipeDown(boolean isDown) {

	}
}
