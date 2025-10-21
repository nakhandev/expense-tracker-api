#!/bin/bash

# 💰 Expense Tracker API Startup Script
# Dynamic and intelligent startup script for the Expense Tracker API

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="Expense Tracker API"
APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_FILE="$APP_DIR/target/expense-tracker-api-1.0.0.jar"
MAIN_CLASS="org.nakhan.ExpenseTrackerApiApplication"
DEFAULT_PORT=8080
PID_FILE="$APP_DIR/expense-tracker.pid"

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check Java version
check_java() {
    print_info "Checking Java installation..."

    if ! command_exists java; then
        print_error "Java is not installed. Please install Java 17 or higher."
        echo "Visit: https://adoptium.net/temurin/releases/"
        exit 1
    fi

    # Check Java version
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
        echo "Please upgrade Java to version 17 or higher."
        exit 1
    fi

    print_success "Java $JAVA_VERSION found"
}

# Function to check Maven
check_maven() {
    print_info "Checking Maven installation..."

    if ! command_exists mvn; then
        print_error "Maven is not installed. Please install Maven 3.6 or higher."
        echo "Visit: https://maven.apache.org/install.html"
        exit 1
    fi

    # Check Maven version
    MAVEN_VERSION=$(mvn -version 2>&1 | head -n1 | awk '{print $3}')
    print_success "Maven $MAVEN_VERSION found"
}

# Function to check port availability
check_port() {
    local port=$1
    print_info "Checking port $port availability..."

    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        print_warning "Port $port is already in use"

        # Try to find the process
        PID=$(lsof -ti:$port)
        if [ ! -z "$PID" ]; then
            print_info "Process $PID is using port $port"
            echo "Process details:"
            ps -p $PID -o pid,ppid,comm,args || true

            read -p "Do you want to kill the process? (y/N): " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                kill $PID 2>/dev/null || true
                sleep 2
                if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
                    print_error "Failed to free port $port"
                    echo "Please manually kill the process or choose a different port."
                    exit 1
                else
                    print_success "Port $port is now available"
                fi
            else
                print_info "Please free port $port manually or specify a different port:"
                echo "  ./start.sh --port 9090"
                exit 1
            fi
        fi
    else
        print_success "Port $port is available"
    fi
}

# Function to build the application
build_application() {
    print_info "Building application..."

    # Check if we need to build
    if [ ! -f "$JAR_FILE" ] || [ "src/main" -nt "$JAR_FILE" ] || [ "pom.xml" -nt "$JAR_FILE" ]; then
        print_info "Building project with Maven..."

        cd "$APP_DIR"
        if mvn clean compile -q; then
            print_success "Application built successfully"
        else
            print_error "Failed to build application"
            echo "Check the Maven output above for errors."
            exit 1
        fi
    else
        print_info "Application is up to date, skipping build"
    fi
}

# Function to start the application
start_application() {
    local port=$1
    print_info "Starting $APP_NAME on port $port..."

    cd "$APP_DIR"

    # Set port if different from default
    if [ $port -ne $DEFAULT_PORT ]; then
        export SERVER_PORT=$port
    fi

    # Start the application in background
    nohup java -jar "$JAR_FILE" > application.log 2>&1 &
    local app_pid=$!

    # Save PID
    echo $app_pid > "$PID_FILE"

    print_info "Application started with PID: $app_pid"

    # Wait for application to start
    print_info "Waiting for application to start..."
    sleep 3

    # Check if application is running
    if ps -p $app_pid > /dev/null 2>&1; then
        print_success "$APP_NAME started successfully!"
        echo ""
        echo "🌐 API URL: http://localhost:$port/api/expenses"
        echo "🗄️  H2 Console: http://localhost:$port/h2-console"
        echo "📝 Application Log: $APP_DIR/application.log"
        echo ""
        echo "Press Ctrl+C to stop the application"
        echo ""

        # Monitor the application
        monitor_application $app_pid
    else
        print_error "Failed to start application"
        echo "Check application.log for details"
        exit 1
    fi
}

# Function to monitor the application
monitor_application() {
    local app_pid=$1

    # Trap SIGINT (Ctrl+C) to gracefully shutdown
    trap 'shutdown_application $app_pid; exit 0' SIGINT

    # Monitor the process
    while ps -p $app_pid > /dev/null 2>&1; do
        sleep 1
    done

    # If we reach here, the application has stopped
    print_warning "Application stopped unexpectedly"
    if [ -f application.log ]; then
        echo "Recent log entries:"
        tail -n 10 application.log
    fi
}

# Function to shutdown the application
shutdown_application() {
    local app_pid=$1

    print_info "Shutting down application (PID: $app_pid)..."

    if ps -p $app_pid > /dev/null 2>&1; then
        kill $app_pid 2>/dev/null || true

        # Wait for graceful shutdown
        for i in {1..10}; do
            if ! ps -p $app_pid > /dev/null 2>&1; then
                break
            fi
            sleep 1
        done

        # Force kill if still running
        if ps -p $app_pid > /dev/null 2>&1; then
            print_warning "Force killing application..."
            kill -9 $app_pid 2>/dev/null || true
        fi

        print_success "Application stopped"
    else
        print_info "Application was not running"
    fi

    # Clean up PID file
    rm -f "$PID_FILE"
}

# Function to show help
show_help() {
    echo "💰 Expense Tracker API Startup Script"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --port PORT     Start application on specified port (default: 8080)"
    echo "  --stop          Stop the running application"
    echo "  --status        Show application status"
    echo "  --restart       Restart the application"
    echo "  --build         Force rebuild the application"
    echo "  --help          Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                    # Start on default port 8080"
    echo "  $0 --port 9090        # Start on port 9090"
    echo "  $0 --stop             # Stop the application"
    echo "  $0 --restart          # Restart the application"
    echo ""
}

# Function to check application status
check_status() {
    if [ -f "$PID_FILE" ]; then
        local app_pid=$(cat "$PID_FILE")
        if ps -p $app_pid > /dev/null 2>&1; then
            print_success "Application is running (PID: $app_pid)"
            return 0
        else
            print_warning "Application is not running (stale PID file)"
            rm -f "$PID_FILE"
            return 1
        fi
    else
        print_info "Application is not running"
        return 1
    fi
}

# Main script logic
main() {
    local port=$DEFAULT_PORT
    local action="start"

    # Parse command line arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --port)
                port="$2"
                shift 2
                ;;
            --stop)
                action="stop"
                shift
                ;;
            --status)
                action="status"
                shift
                ;;
            --restart)
                action="restart"
                shift
                ;;
            --build)
                action="build"
                shift
                ;;
            --help)
                show_help
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                show_help
                exit 1
                ;;
        esac
    done

    # Handle different actions
    case $action in
        "start")
            print_info "Starting $APP_NAME..."
            check_java
            check_maven
            check_port $port
            build_application
            start_application $port
            ;;
        "stop")
            if [ -f "$PID_FILE" ]; then
                local app_pid=$(cat "$PID_FILE")
                shutdown_application $app_pid
            else
                print_info "No PID file found. Application may not be running."
            fi
            ;;
        "status")
            check_status
            ;;
        "restart")
            print_info "Restarting $APP_NAME..."
            if [ -f "$PID_FILE" ]; then
                local app_pid=$(cat "$PID_FILE")
                shutdown_application $app_pid
                sleep 2
            fi
            check_java
            check_maven
            check_port $port
            build_application
            start_application $port
            ;;
        "build")
            print_info "Building $APP_NAME..."
            check_java
            check_maven
            build_application
            print_success "Build completed successfully"
            ;;
    esac
}

# Run the main function with all arguments
main "$@"
