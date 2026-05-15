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



Current V1 limitation:
Payload data must not contain the '|' delimiter character.

Future protocol versions may migrate to JSON serialization for safer structured communication.