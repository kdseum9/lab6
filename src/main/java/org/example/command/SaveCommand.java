package org.example.command;

import org.example.manager.CollectionManager;
import org.example.share.Request;
import org.example.share.Response;

public class SaveCommand extends AbstractCommand {

    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        collectionManager.getXmlManipulator().write();
        logger.info("Save command executed successfully.");
        return new Response("Collection saved to file.", null);
    }
}
