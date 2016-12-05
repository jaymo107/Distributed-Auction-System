import javax.crypto.SealedObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
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
        this.loadServerKeys();
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
     * Remove the auction from the hash map and verifyClient the constraints
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
    public SignedObject verifyClient(Auth authObject) {

        Auth auth = authObject;
        String randomValue = authObject.getValue();

        auth.setOriginIp(this.getFormattedIPAddress());
        Signature signature = null;
        SignedObject obj = null;

        try {
            /**
             * SignedObject method
             */
            byte[] bytes = auth.getValue().getBytes();

            signature = Signature.getInstance("DSA");
            //signature.initSign(this.privateKey);
            obj = new SignedObject(auth, this.privateKey, signature);

            System.out.println(obj.verify(this.publicKey, signature));
//           signature.sign();

            /**
             * SealedObject
             */
            //Cipher cipher = Cipher.getInstance("SHA1withDSA", "SUN");
//            cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
//            byte[] encrypted = cipher.doFinal(randomValue.getBytes());
//            auth.setValue(Base64.encode(encrypted));

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Load the public key of either a user or the server.
     *
     * @param user
     * @return
     */
    private PublicKey loadPublicKey(User user) {
        DSAPublicKey pk = null;
        String location = (user == null) ? "server" : String.valueOf(user.getId());

        try {
            FileInputStream fs = new FileInputStream(new File("./keys/public/" + location + ".key"));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] puKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            KeySpec ks = new X509EncodedKeySpec(puKey);
            pk = (DSAPublicKey) keyFactory.generatePublic(ks);
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
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            KeySpec ks = new PKCS8EncodedKeySpec(prKey);
            this.privateKey = (DSAPrivateKey) keyFactory.generatePrivate(ks);
            this.publicKey = loadPublicKey(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the formatted IP address to append to the payload
     * of the auth object.
     *
     * @return
     */
    private String getFormattedIPAddress() {

        try {
            return Inet4Address.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

}
