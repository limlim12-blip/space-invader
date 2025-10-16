import os
import numpy as np
import subprocess
import tensorflow as tf
import time
import gymnasium as gym
from Env import SpaceInvaderEnv
from stable_baselines3 import DQN,PPO
from stable_baselines3.common.vec_env import DummyVecEnv, SubprocVecEnv
from stable_baselines3.common.env_util import make_vec_env

MODEL_DIR= f'train'
LOG_DIR= f'logs'

proc = subprocess.Popen(
    ["mvn","clean","javafx:run"],
    stdout=subprocess.DEVNULL,
    stderr=subprocess.DEVNULL
)
time.sleep(5)
env =SpaceInvaderEnv()
env.reset()

model = PPO('MultiInputPolicy',env,verbose=1,tensorboard_log=LOG_DIR)

for i in range(1,1000000):
    model.learn(total_timesteps=10000,reset_num_timesteps=False,tb_log_name="PPO")
    model.save(f"{MODEL_DIR}/{10000*i}")
env.close()
proc.terminate()