import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Handles the bids and auctions from the various clients
 * and passes messages between the hosts.
 *
 * @author JamesDavies
 */
public class AuctionServiceImpl extends UnicastRemoteObject implements BuyerService, SellerService {

    private HashMap<Integer, Auction> auctions;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private HashMap<Integer, User> authenticatedUsers;

    public AuctionServiceImpl() throws RemoteException {
        super();
        this.auctions = new HashMap<>();
        this.authenticatedUsers = new HashMap<>();

        // Generate the key pair for use with authentication
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keys = generator.generateKeyPair();
            this.publicKey = keys.getPublic();
            this.privateKey = keys.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new auction and store it in the 'Auctions' hashmap,
     * generate an index key and return that ID.
     *
     * @param item   The item within the auction to bid for.
     * @param seller The seller identifier.
     * @return String     Return the Auction message
     */
    public String createAuction(Item item, User seller) throws RemoteException {
        Auction auction = new Auction(item, seller);
        int key = auction.getId();
        auctions.put(key, auction);
        return "Auction created successfully with ID " + key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @param user      The user making the bid.
     * @return int      The success number.
     */
    public String bid(int auctionId, BigDecimal amount, User user) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        return auctions.get(auctionId).bid(amount, user);
    }

    /**
     * Remove the auction from the hash map and verify the constraints
     * pass such as reserve price etc.
     *
     * @param auctionId The ID of the auction to close.
     * @param seller    The User object of the requesting close.
     * @return String   The message from the server.
     * @throws RemoteException
     */
    public String closeAuction(int auctionId, User seller) throws RemoteException {

        // Store the current auction requested.
        Auction auction = this.auctions.get(auctionId);

        // Check if the auction exists.
        if (auction == null) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        // The current bid is below the reserve price.
        if (auction.getCurrentBid().compareTo(auction.getItem().getReservePrice()) == -1) {
            this.auctions.remove(auctionId);
            return "MESSAGE: Closing auction, reserve price not met.";
        }

        // Store the auction winner and highest bid
        User winner = auction.getHighestBidder();
        BigDecimal winningBid = auction.getCurrentBid();

        this.auctions.remove(auctionId);
        return "MESSAGE: Item sold! The winner of this auction was " + winner.getId() + " with £" + winningBid.toString();
    }

    /**
     * Display a list of currently active auctions.
     *
     * @return String Display the list of auctions.
     * @throws RemoteException
     */
    public String browseAuctions() throws RemoteException {
        if (this.auctions.size() <= 0) {
            return "NOTICE: There are currently no auctions in place.";
        }

        StringBuilder builder = new StringBuilder();

        // Loop over each element in the hashmap and print out the values for each.

        Iterator it = this.auctions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Auction a = this.auctions.get(pair.getKey());

            builder.append("Auction ID: [" + a.getId() + "]\n");
            builder.append("Created: " + a.getCreatedAt().toString() + "\n");
            builder.append("Description: " + a.getItem().getDescription() + "\n");
            builder.append("Current Bid: £" + a.getCurrentBid() + "\n");
            builder.append("-------------------------------------------------------------------\n\n");
        }

        return builder.toString();
    }

    /**
     * Recieves the Auth object, encrypts it, signs it and returns it.
     *
     * @param authObject
     * @return
     */
    public Signature verify(Auth authObject) {
        // Encrypt the message
        byte[] bytes = authObject.getValue().getBytes();
        Signature signature = null;
        try {

            signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(this.privateKey, new SecureRandom());

            return signature;

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Sign it
        return signature;
        // Send it back to the client
    }

    /**
     * Load the public key of either a user or the server.
     *
     * @param user
     * @return
     */
    private PublicKey loadPublicKey(User user) {
        RSAPublicKey pk = null;
        String location = (user == null) ? "server" : String.valueOf(user.getId());

        try {
            FileInputStream fs = new FileInputStream(new File("./keys/public/" + location + ".key"));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] puKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new X509EncodedKeySpec(puKey);
            pk = (RSAPublicKey) keyFactory.generatePublic(ks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pk;
    }

    /**
     * Load the servers public and private key.
     */
    private void loadServerKeys() {
        try {
            FileInputStream fs = new FileInputStream(new File("./keys/private/server.key"));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] prKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new PKCS8EncodedKeySpec(prKey);
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);
            this.publicKey = loadPublicKey(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
