import java.rmi.RemoteException;

public interface AuctionClient {

    /**
     * Get the input from the CLI.
     * @throws RemoteException
     */
    void getInput() throws RemoteException;

    /**
     * Execute the appropriate command.
     * @param command The command that should be executed.
     * @throws RemoteException
     */
    void executeCommand(String command) throws RemoteException;
}
