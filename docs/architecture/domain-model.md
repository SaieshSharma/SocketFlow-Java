Node

Fields:

nodeId
hostname
status
lastHeartbeat
osType
ipAddress

Responsibilities:

represent connected machine
track health state
Command

Fields:

commandId
targetNode
commandText
status
createdAt
result

Responsibilities:

represent remote command execution
Heartbeat

Fields:

timestamp
cpuUsage
memoryUsage

Responsibilities:

represent node health signal