package com.game.thecrown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.game.thecrown.scene2d.Floor;
import com.game.thecrown.scene2d.Player;
import com.game.thecrown.scene2d.Rock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruben on 2/11/17.
 */

public class GameScreen extends BaseScreen implements ContactListener {

	private Box2DDebugRenderer renderer;
	private OrthographicCamera camera;
	private Stage stage;
	private World world;
	private Player player;
	private boolean dropping;
	private List<Floor> floorList = new ArrayList<Floor>();

	public GameScreen(GameMain game) {
		super(game);
		stage = new Stage(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		renderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(16, 9);
		camera.translate(6, 1);
		world = new World(new Vector2(0, -10f), true);

	}

	@Override
	public void show() {

		player = new Player("runner.png", world);
//		rock = new Rock(world);

		Texture floorTexture = new Texture(Gdx.files.internal("rock.png"));

		floorList.add(new Floor(world, floorTexture, -200, -2, 1000));
		floorList.add(new Floor(world, floorTexture, 5, -1, 3));
		floorList.add(new Floor(world, floorTexture, 10, -1, 2));
//		floorList.add(new Floor(world, floorTexture, 1, 2, 10 ));

		stage.addActor(player);
		for (Floor floor : floorList) {
			stage.addActor(floor);
		}

		world.setContactListener(this);
//		stage.addActor(rock);
	}


	@Override
	public void hide() {
		player.detach();
		player.remove();
		for (Floor floor : floorList) {
			floor.detach();
			floor.remove();
		}
//		rock.detach();
//		rock.remove();
	}

	@Override
	public void dispose() {
		stage.dispose();
		world.dispose();
		renderer.dispose();
	}

	@Override
	public void render(float v) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();


//		if (player.isAlive()) {
//			float velocityY = rock.getRockBody().getLinearVelocity().y;
//			rock.getRockBody().setLinearVelocity(8, velocityY);
//		}
//
//		if (player.isAlive()) {
//			stage.getCamera().translate(Constants.PLAYER_SPEED * v * Constants.PIXELS_IN_METER, 0, 0);
//		}

		world.step(v, 6, 2);
		camera.update();
		renderer.render(world, camera.combined);
	}

	@Override
	public void beginContact(Contact contact) {

		if (areCollided(contact, Constants.PLAYER_USER_DATA, Constants.FLOOR_USER_DATA)) {
			player.setJumping(false);
		}

	}

	private boolean areCollided(Contact contact, Object userA, Object userB) {
		return ((contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) ||
				(contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userB)));
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold manifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse contactImpulse) {

	}
}
