#!/bin/bash
set -e

# Wait for MySQL to be ready using wait-for-it.sh script
/wait-for-it.sh mysql:3306 --timeout=30 --strict -- echo "MySQL is up - executing command"

# Execute the main application process
exec java -jar app.jar
