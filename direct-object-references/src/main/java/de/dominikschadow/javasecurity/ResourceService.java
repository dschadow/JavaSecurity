package de.dominikschadow.javasecurity;

import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.reference.RandomAccessReferenceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ResourceService {
    private static final Logger log = LoggerFactory.getLogger(ResourceService.class);
    private final Set<Object> resources = new HashSet<>();
    private final RandomAccessReferenceMap referenceMap = new RandomAccessReferenceMap(resources);
    private final String rootLocation;

    public ResourceService() {
        this.rootLocation = "http://localhost:8080/files/";
    }

    @PostConstruct
    protected void init() {
        File coverImage = new File("cover.pdf");
        referenceMap.addDirectReference(coverImage);
        resources.add(coverImage);

        File coverPdf = new File("cover.jpg");
        referenceMap.addDirectReference(coverPdf);
        resources.add(coverPdf);
    }

    public Set<String> getAllIndirectReferences() {
        Set<String> indirectReferences = new HashSet<>();

        for (Object file : resources) {
            String indirectReference = referenceMap.getIndirectReference(file);
            indirectReferences.add(indirectReference);
        }

        return indirectReferences;
    }

    public File getFileByIndirectReference(String indirectReference) throws AccessControlException {
        File file = referenceMap.getDirectReference(indirectReference);

        log.info("File name {}", file.getName());

        return file;
    }

    public Resource loadAsResource(String filename) throws MalformedURLException {
        Resource resource = new UrlResource(rootLocation + filename);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }

        return null;
    }
}