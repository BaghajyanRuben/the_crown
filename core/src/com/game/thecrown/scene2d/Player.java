package com.game.thecrown.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.thecrown.Constants;
import com.game.thecrown.SwipeDetector;

/**
 * Created by ruben on 2/11/17.
 */

public class Player extends Actor implements SwipeDetector.SwipeListener {

	private String playerAsset;
	private Vector2 position;
	private static final int col = 5;
	private static final int row = 2;
	private GestureDetector gestureDetector;
	private Animation<TextureRegion> animation;
	private Texture playerTexture;
	private TextureRegion[] frames;
	private TextureRegion currentFrames;
	private int playerWidth;
	private int playerHeight;
	private World world;
	private Body body;
	private Fixture fixture;
	private boolean alive = true;
	private boolean jumping = false;
	private float stateTime;
	private boolean isSwipeUp = false;
	private boolean isSwipeDown = false;
	private boolean slipping = false;


	public Player(String playerAsset, World world) {
		this.playerAsset = playerAsset;
		this.world = world;
		playerTexture = new Texture(Gdx.files.internal(playerAsset));
		playerWidth = playerTexture.getWidth() / col;
		playerHeight = playerTexture.getHeight() / row;
		position = new Vector2(0.5f, 0.5f);

		TextureRegion[][] tmp = TextureRegion.split(playerTexture, playerWidth, playerHeight);
		frames = new TextureRegion[col * row];

		int index = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				frames[index++] = tmp[i][j];
			}

		}
		animation = new Animation(1 / 15f, frames);

		gestureDetector = new GestureDetector(new SwipeDetector(this));
		Gdx.input.setInputProcessor(gestureDetector);

		body = world.createBody(createPlayerBodyDef());


		fixture = createPlayerFixture();
		fixture.setUserData(Constants.PLAYER_USER_DATA);

		currentFrames = animation.getKeyFrame(stateTime);

		setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);

	}


	private BodyDef createPlayerBodyDef() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(position);
		return def;
	}

	private Fixture createPlayerFixture() {
		PolygonShape box = new PolygonShape();
		box.setAsBox(position.x, position.y);
		Fixture fixture = body.createFixture(box, 3);
		box.dispose();
		return fixture;
	}


	@Override
	public void act(float delta) {
		stateTime += delta;
		if (isSwipeUp) {
			jump();
			isSwipeUp = false;
		}
		currentFrames = animation.getKeyFrame(stateTime, true);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setPosition((body.getPosition().x )* Constants.PIXELS_IN_METER,
					(body.getPosition().y + 2f)* Constants.PIXELS_IN_METER);
		batch.draw(currentFrames, getX(), getY(), getWidth(), getHeight());
	}


	public void detach() {
		playerTexture.dispose();
		body.destroyFixture(fixture);
		world.destroyBody(body);
	}

	public Body getBody() {
		return body;
	}

	public Fixture getFixture() {
		return fixture;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public int getPlayerWidth() {
		return playerWidth;
	}

	public int getPlayerHeight() {
		return playerHeight;
	}

	public Vector2 getPosition() {
		return position;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	public Texture getPlayerTexture() {
		return playerTexture;
	}

	public TextureRegion getCurrentFrames() {
		return currentFrames;
	}

	public TextureRegion[] getFrames() {
		return frames;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void jump() {
		if (!jumping) {
			jumping = true;
			Vector2 position = getPosition();
			body.applyLinearImpulse(0, 20, position.x, position.y, true);
		}
	}


	@Override
	public void onSwipeUp(boolean isUp) {
		isSwipeUp = isUp;
	}

	@Override
	public void onSwipeDown(boolean isDown) {
		isSwipeDown = true;
		slipping = true;
	}
}
