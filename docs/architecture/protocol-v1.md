TYPE|NODE_ID|PAYLOAD


# Supported Messages

Register
REGISTER|node-01|{"os":"linux"}

HeartBeat
HEARTBEAT|node-01|{"cpu":23}

COMMAND
COMMAND|master|ls -la

COMMAND RESULT
COMMAND_RESULT|node-01|{"output":"..."}


Failure behavior.

malformed packets
unknown message types
oversized payloads
disconnected nodes

WIP



