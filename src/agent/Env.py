
import time
import gymnasium as gym
from gymnasium import Env
from gymnasium import spaces
import numpy as np
import random
from py4j.java_gateway import JavaGateway, GatewayParameters


class SpaceInvaderEnv(gym.Env):
    def __init__(self):
        super(SpaceInvaderEnv, self).__init__()

        self.gateway = JavaGateway(gateway_parameters=GatewayParameters(port=25333))
        self.game_java = self.gateway.entry_point

        self.observation_space = spaces.Dict(
            {
                "Player":spaces.Box(low=-np.inf, high=np.inf, shape=(2,), dtype=np.float64),
                "Boss":spaces.Box(low=-np.inf, high=np.inf, shape=(2,), dtype=np.float64),
                "Star":spaces.Box(low=-np.inf, high=np.inf, shape=(2,), dtype=np.float64),



                "Enemies":spaces.Box(low=-np.inf, high=np.inf, shape=(20,), dtype=np.float64),
                "Moon":spaces.Box(low=-np.inf, high=np.inf, shape=(20,), dtype=np.float64),
                "Ebullets":spaces.Box(low=-np.inf, high=np.inf, shape=(40,), dtype=np.float64),
            }
        )

        #[0: stay, 1: move left, 2: move right, 3: shoot]
        self.action_space = spaces.Discrete(4)

        self.state = None
        self.steps=0


    def get_obs(self):
        self.player = self.game_java.getPlayer()#return player
        self.enemies = self.game_java.getEnemies() #2D array[][x/y]
        self.star = self.game_java.getUp()#2D array[][x/y]
        self.boss = self.game_java.getBoss()#2D array[][x/y]
        self.Moon = self.game_java.getMoon()#2D array[][x/y]
        self.eBullets = self.game_java.geteBullets()#2D array[][x/y]

        
        obs = {
                "Player":np.array([self.player.getX(),self.player.getY()]),
                "Boss": np.array(self.boss).flatten(),
                "Star": np.array(self.star).flatten(),



                "Enemies": np.array(self.enemies).flatten(),
                "Moon": np.array(self.Moon).flatten(),
                "Ebullets": np.array(self.eBullets).flatten(),
            }
        
        return obs

    def step(self, action):
        self.game_java.Action(int(action))


        # Compute reward
        reward = self.get_reward()

        # Check done
        done = bool(self.game_java.getGameOver())
        self.steps+=1
        info = {self.steps}

        return self.get_obs, reward, done, False, info

    def reset(self, seed = None):
        """Reset the Java game and return initial state."""
        super().reset(seed=seed)
        self.game_java.startGame()
        self.state = self.get_obs()
        return self.state, {}

    def close(self):
        self.gateway.close() 
    def get_reward(self):
        self.reward = self.game_java.getReward()
        return self.reward
        
