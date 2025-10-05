
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

        obs_dim = 6  
        self.observation_space = spaces.Box(low=-np.inf, high=np.inf, shape=(obs_dim,), dtype=np.float32)

        #[0: stay, 1: move left, 2: move right, 3: shoot]
        self.action_space = spaces.Discrete(4)

        self.state = None

    def get_obs(self):
        self.player = self.game_java.getPlayer()
        self.enemies = self.game_java.getEnemies()
        self.boss = self.game_java.getBoss()
        self.Moon = self.game_java.getMoon()
        self.eBullets = self.game_java.geteBullets()

        obs = np.concatenate((
            np.array([
                self.player.getX(),
                self.player.getY(),
                self.player.getHealth(),
                1.0 if self.game_java.getGameOver() else 0.0,
            ]),
            np.array(self.enemies).flatten(),
            np.array(self.Moon).flatten(),
            np.array(self.eBullets).flatten(),
        ))
        return obs

    def step(self, action):
        self.game_java.Action(int(action))

        # Get updated state
        self.state = self.get_obs()

        # Compute reward
        reward = int(self.game_java.getReward())

        # Check done
        done = bool(self.game_java.getGameOver())

        # Optional info dict
        info = {}

        return self.state, reward, done, False, info

    def reset(self, *, seed=None, options=None):
        """Reset the Java game and return initial state."""
        super().reset(seed=seed)
        self.game_java.resetGame()
        self.state = self.get_obs()
        return self.state, {}

    def close(self):
        self.gateway.close() 
    def _get_reward(self):
        return 1.0
        
env = SpaceInvaderEnv()
obs, info = env.reset()
done = False
while not done:
    action = env.action_space.sample()
    obs, reward, done, trunc, info = env.step(action)
    print(obs, reward)                               
