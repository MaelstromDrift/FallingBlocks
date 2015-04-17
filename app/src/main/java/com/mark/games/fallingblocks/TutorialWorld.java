package com.mark.games.fallingblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mark.games.fallingblocks.framework.collision.ContinuousCollision;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.collision.SpatialHashGrid;

public class TutorialWorld {
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_GAMEOVER = 3;
	public int state;
	public int score;

	public boolean lavaMoving;

	public SpatialHashGrid grid;
	public Lava lava;
	public SquareMan character;
	public List<Block> blocks;

	Random ran;

	public TutorialWorld() {
		grid = new SpatialHashGrid(5000, 5000, 200);
		lavaMoving = false;
		ran = new Random();
		lava = new Lava(160, -480, 320, 480);
		character = new SquareMan(160, 32, 32, 50);
		blocks = new ArrayList<Block>();
		state = GAME_RUNNING;
		createBlocks();
	}

	public void createBlocks() {
		blocks.add(new Block(100, 50, 100, 100));
		blocks.add(new Block(150, 151, 100, 100));
		blocks.add(new Block(150, 335, 100, 100));
		blocks.get(2).state = Block.STATE_STOPPED;
		blocks.get(2).velocity.y = -100;
	}

	public void update(float deltaTime, float accelX) {
		if (state == GAME_RUNNING) {
			if (character.position.y > 400) {
				lavaMoving = true;
			}
			if (lavaMoving) {
				updateLava(deltaTime);
			}
			updateChar(deltaTime, accelX);
			updateBlocks(deltaTime);
		}
	}

    public void updateChar(float deltaTime, float accelX) {
        character.update(deltaTime);
        character.velocity.x = -(accelX * 100);
    }

	public void updateBlocks(float deltaTime) {
		int len = blocks.size();
		for (int i = 0; i < len; i++) {
			Block block = blocks.get(i);
			block.update(deltaTime);
		}
	}

	public void updateLava(float deltaTime) {
		lava.update(deltaTime);
	}

	public void checkCollisions(float deltaTime) {
		if (state == GAME_RUNNING) {
			checkCharacterCollisions(deltaTime);
			checkBoxCollision(deltaTime);
			checkLavaCollision();
		}
	}

    public void checkCharacterCollisions(float deltaTime) {
        for (int i = 0; i < blocks.size(); i++) {
            if (ContinuousCollision.checkYCollision(character.position.y,
                    character.bounds.height / 2, character.velocity.y
                    * deltaTime, character.acceleration.y * deltaTime,
                    blocks.get(i).position.y, blocks.get(i).height / 2)) {
                if (character.velocity.y <= 0.0f)
                    if (character.position.x - character.bounds.width / 2 < blocks
                            .get(i).position.x + blocks.get(i).width / 2
                            && character.position.x + character.bounds.width
                            / 2 > blocks.get(i).position.x
                            - blocks.get(i).width / 2) {
                        character.hitTop(blocks.get(i).position.y,
                                blocks.get(i).width, blocks.get(i).velocity.y);

                        break;
                    }

                if (character.position.x - character.bounds.width / 2 < blocks
                        .get(i).position.x + (blocks.get(i).width / 2) - 10
                        && character.position.x + character.bounds.width / 2 > blocks
                        .get(i).position.x
                        - (blocks.get(i).width / 2)
                        + 10) {
                    character.hitBottomPlatform(blocks.get(i).position.y
                            - blocks.get(i).width / 2, blocks.get(i).velocity.y
                            * deltaTime);

                    break;
                }
            } else if (character.state == SquareMan.STATE_STILL) {
                character.state = SquareMan.STATE_MOVING;
                break;
            }
        }

        // Hitting the side
        for (int i = 0; i < blocks.size(); i++) {
            if (OverlapTester.overlapRectangles(character.bounds,
                    blocks.get(i).bounds)) {
                if (character.position.y - 25 < blocks.get(i).position.y
                        + blocks.get(i).width / 2
                        && character.position.y + 25 > blocks.get(i).position.y
                        - blocks.get(i).width / 2) {
                    if (character.position.x > blocks.get(i).position.x) {
                        character.hitRight(blocks.get(i).position.x,
                                blocks.get(i).width / 2);
                    }
                }

                if (character.position.y - 25 < blocks.get(i).position.y
                        + blocks.get(i).width / 2
                        && character.position.y + 25 > blocks.get(i).position.y
                        - blocks.get(i).width / 2) {
                    if (character.position.x + 32 < blocks.get(i).position.x) {
                        character.hitLeft(blocks.get(i).position.x,
                                blocks.get(i).width / 2);
                    }
                }
            }
            // checking a frame ahead
            if (ContinuousCollision.checkXCollision(character.position.x,
                    character.bounds.width / 2, character.velocity.x
                    * deltaTime, blocks.get(i).position.x,
                    blocks.get(i).width / 2)) {
                if (character.position.y - 25 < blocks.get(i).position.y
                        + blocks.get(i).width / 2
                        && character.position.y + 25 > blocks.get(i).position.y
                        - blocks.get(i).width / 2) {
                    if (character.position.x - 32 > blocks.get(i).position.x) {
                        character.hitRight(blocks.get(i).position.x, blocks.get(i).width / 2);
                        //break;
                    }
                }

                if (character.position.y - 25 < blocks.get(i).position.y
                        + blocks.get(i).width / 2
                        && character.position.y + 25 > blocks.get(i).position.y
                        - blocks.get(i).width / 2) {
                    if (character.position.x + 32 <= blocks.get(i).position.x) {
                        character.hitLeft(blocks.get(i).position.x,	blocks.get(i).width / 2);
                        //break;
                    }
                }
            }
        }
    }

    public void checkBoxCollision(float deltaTime) {
        for (int j = 0; j < blocks.size(); j++) {
            for (int i = 0; i < blocks.size(); i++) {
                if (i != j){
                    if (ContinuousCollision.checkYCollision(
                            blocks.get(j).position.y, 50,
                            blocks.get(j).velocity.y * deltaTime, 0,
                            blocks.get(i).position.y, 50)) {
                        if (blocks.get(j).position.x - blocks.get(j).width / 2 < blocks
                                .get(i).position.x
                                + (blocks.get(i).width / 2)
                                - 2
                                && blocks.get(j).position.x
                                + (blocks.get(j).width / 2) - 2 > blocks
                                .get(i).position.x
                                - blocks.get(i).width / 2) {

                            blocks.get(j).hitBlock(
                                    blocks.get(i).position.y
                                            + blocks.get(i).width);
                            break;
                        }
                    }
                }
                if (ContinuousCollision.checkYCollision(blocks.get(i).position.y, 50, blocks.get(i).velocity.y
                        * deltaTime, 0, character.position.y, 25)) {
                    if (blocks.get(i).position.x - (blocks.get(i).width / 2)
                            - 4 < character.position.x + 16
                            && blocks.get(i).position.x
                            + (blocks.get(i).width / 2) - 4 > character.position.x - 16) {
                        if (character.state == SquareMan.STATE_STILL) {
                            character.state = SquareMan.STATE_DEAD;
                            state = GAME_GAMEOVER;
                            break;
                        }
                    }
                }
            }
        }
    }

	public void checkLavaCollision() {
		if (OverlapTester.overlapRectangles(lava.bounds, character.bounds)) {
			character.state = SquareMan.STATE_DEAD;
			state = GAME_GAMEOVER;
		}
	}
}
