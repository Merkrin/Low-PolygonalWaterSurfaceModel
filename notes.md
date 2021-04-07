# Boundaries
| Setting | Minimum | Maximum | Needs check? |
| ------- | ------- | ------- | ------------ |
| FPS_CAP | 30 | 120 | No |
| WORLD_SIZE | 100 | 1000 | No |
| SEED | Integer.MIN_VALUE | Integer.MAX_VALUE | No |
| AMPLITUDE | 10.0f | 100.0f | Yes |
| ROUGHNESS | 0.1f | 0.9f | No |
| OCTAVES | 2 | 10 | No |
| COLOR_SPREAD | 0.1f | 0.9f | No |
| WATER_HEIGHT | -10 | 10 | Yes |
| WAVE_SPEED | 0.0005f | 0.009f | No |
| WAVE_LENGTH | 1 | 10 | No |
| WAVE_AMPLITUDE | 0.05f | 0.9f | Yes |

#Standard settings
| Setting | Value |
| ------- | ----- |
| FPS_CAP | 100 |
| COLOR_SPREAD | 0.45f |
| LIGHT_POSITION | (0.3f, -1f, 0.5f) |
| LIGHT_BIAS | (0.3f, 0.8f) |
| WORLD_SIZE | 200 |
| AMPLITUDE | 30 |
| ROUGHNESS | 0.4f |
| OCTAVES | 5 |
| WATER_HEIGHT | -1 |
| WAVE_SPEED | 0.002f |
| WAVE_LENGTH | 4.0f |
| WAVE_AMPLITUDE | 0.2f |

#Example starter
-XstartOnFirstThread -fps 100 -w 500 -h 500 -WS 500 -S 200 -A 10 -R 0.5 -O 5 -cs 0.45 -tc 10;10;10;20;20;20;30;30;30;40;40;40
-lp
0.50;0.5;0.5
-lc
1;0.9;0.9
-lb
0.3;0.8
-wh
-1
-ws
0.005
-wl
1
-wa
0.4

#Keyboard shortcuts
| Shortcut | Variable | Value |
| -------- | -------- | ----- |
| 0 | showWater | !showWater |
| - | animateWater | !animateWater |
| H | help |  |
| P | save |  |
| 1 | waveAmplitude | +0.1 |
| 2 | waveAmplitude | -0.1 |
| 3 | waveLength | +1 |
| 4 | waveLength | -1 |
| 5 | waveSpeed | +0.001 |
| 6 | waveSpeed | -0.001 |