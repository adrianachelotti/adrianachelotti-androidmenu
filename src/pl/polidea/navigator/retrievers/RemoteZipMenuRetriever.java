package pl.polidea.navigator.retrievers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Retriever which reads menu from zip file downloaded over http.
 */
public class RemoteZipMenuRetriever extends AbstractMenuRetrieverBase implements MenuRetrieverInterface {

    private static final String TAG = RemoteZipMenuRetriever.class.getSimpleName();

    private static final int BUFFER_SIZE = 8192;

    private final URL whereToDownloadFrom;
    private final boolean wifiOnly;

    public RemoteZipMenuRetriever(final Context ctx, final URL whereToDownloadFrom, final boolean wifiOnly,
            final String toInternalLocation) {
        super(ctx, toInternalLocation);
        this.whereToDownloadFrom = whereToDownloadFrom;
        this.wifiOnly = wifiOnly;
    }

    public boolean isOnWifi() {
        final ConnectivityManager mgrConn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network = mgrConn.getActiveNetworkInfo();
        return network != null && network.getType() == ConnectivityManager.TYPE_WIFI;
    }

    @Override
    public String getMenuSignature() throws IOException {
        if (!wifiOnly || isOnWifi()) {
            Log.d(TAG, "Skipping connection - we are not on wifi only");
            return getOldSignature();
        }
        final HttpURLConnection conn = (HttpURLConnection) whereToDownloadFrom.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setConnectTimeout(5 * 1000);
        conn.connect();
        final Map<String, List<String>> fields = conn.getHeaderFields();
        Log.d(TAG, "Retrieved headers: " + fields);
        final List<String> etagHeaders = fields.get("ETag");
        final List<String> sizeHeaders = fields.get("Content-Length");
        final List<String> lastModifiedHeaders = fields.get("Last-Modified");
        String signature = null;
        if (!etagHeaders.isEmpty()) {
            signature = whereToDownloadFrom.toExternalForm() + ":" + etagHeaders.get(0);
        } else if (!lastModifiedHeaders.isEmpty()) {
            signature = whereToDownloadFrom.toExternalForm() + ":" + lastModifiedHeaders.get(0);
        } else if (!sizeHeaders.isEmpty()) {
            signature = whereToDownloadFrom.toExternalForm() + ":" + sizeHeaders.get(0);
        } else {
            signature = whereToDownloadFrom.toExternalForm();
        }
        return signature;
    }

    @Override
    protected void copyMenuInternally() throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) whereToDownloadFrom.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.connect();
        final ZipInputStream is = new ZipInputStream(conn.getInputStream());
        unpackTheZipFile(is);
    }

    /**
     * Unpacks the zip file to the directory, cleaning it up beforehand.
     * 
     * @param inputStream
     *            zip stream
     * @param directory
     *            directory to unpack file to
     * @throws IOException
     *             in case of problems with unpacking
     * @throws CanceledByUserException
     *             when canceled
     */
    private void unpackTheZipFile(final ZipInputStream inputStream) throws IOException {
        long currentSize = 0;
        cleanUpDirectory(internalTmpDirectory);
        ZipEntry zipentry = inputStream.getNextEntry();
        final byte[] buf = new byte[BUFFER_SIZE];
        while (zipentry != null) {
            final String zipEntryName = zipentry.getName();
            if (zipentry.isDirectory()) {
                final File dir = new File(internalTmpDirectory, zipEntryName);
                if (!dir.mkdir()) {
                    throw new IOException("Could not create directory:" + dir);
                }
                inputStream.closeEntry();
                zipentry = inputStream.getNextEntry();
                continue;
            }
            final File newFile = new File(internalTmpDirectory, zipEntryName);
            final FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            try {
                int n = -1;
                while ((n = inputStream.read(buf, 0, BUFFER_SIZE)) > -1) {
                    fileOutputStream.write(buf, 0, n);
                }
            } finally {
                fileOutputStream.close();
            }
            currentSize += zipentry.getCompressedSize();
            inputStream.closeEntry();
            zipentry = inputStream.getNextEntry();
        }
    }
}
