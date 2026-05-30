import socket

def start_pc_server():
    # Use '0.0.0.0' to listen on all available network interfaces
    host = '0.0.0.0' 
    port = 5001  # Choose an open port
    
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)
    server_socket.bind((host, port))
    server_socket.listen(1)
    
    print(f"Server started. Listening on port {port}...")
    print("Please check your PC's local IP address (e.g., 192.168.1.X)")

    client_socket, client_address = server_socket.accept()
    print(f"Connected to Android device at: {client_address}")

    try:
        while True:
            # Get message from PC keyboard
            message = input("Type message to send to phone: ")
            if not message:
                continue
            
            # Send message with a newline character as a delimiter
            client_socket.sendall((message + "\n").encode('utf-8'))
            
            if message.lower() == 'exit':
                break
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()
        server_socket.close()
        print("Connection closed.")

if __name__ == "__main__":
    start_pc_server()