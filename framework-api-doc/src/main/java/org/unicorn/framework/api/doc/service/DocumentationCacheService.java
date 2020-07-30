package org.unicorn.framework.api.doc.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.DocumentationCache;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-30 10:51
 */
@Service
@Slf4j
public class DocumentationCacheService {

    @Autowired
    private DocumentationCache documentationCache;

    public Documentation documentationByGroup(String groupName) {
        return documentationCache.documentationByGroup(groupName);
    }

    public Documentation documentationByGroup(String groupName, String keyword) {
        Documentation document = documentationByGroup(groupName);
        if (StringUtils.isBlank(keyword)) {
            return document;
        }
        List<ApiDescription> newApis = new ArrayList<>();

        Multimap<String, ApiListing> newApiListings = ArrayListMultimap.create();
        Set<Tag> srcTags = new HashSet<>(document.getTags());
        Set<Tag> newTags = new HashSet<>();
        ResourceListing newResourceListing = document.getResourceListing();
        Set<String> newProduces = new HashSet<>(document.getProduces());
        Set<String> newConsumes = new HashSet<>(document.getConsumes());
        Set<String> newSchemes = new HashSet<>(document.getSchemes());

        Multimap<String, ApiListing> apiListings = document.getApiListings();
        Iterator<Map.Entry<String, ApiListing>> iterator = apiListings.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ApiListing> next = iterator.next();
            String key = next.getKey();
            ApiListing apiList = next.getValue();
            List<ApiDescription> apis = apiList.getApis();
            for (ApiDescription api : apis) {
                if (isExistApi(newTags, api, keyword)) {
                    newApis.add(api);
                }
            }
            ApiListing newApiListing = new ApiListing(apiList.getApiVersion(), apiList.getBasePath(),
                    apiList.getResourcePath(), apiList.getProduces(), apiList.getConsumes(), apiList.getHost(),
                    apiList.getProtocols(), apiList.getSecurityReferences(), newApis, apiList.getModels(),
                    apiList.getDescription(), apiList.getPosition(), apiList.getTags());
            newApiListings.put(key, newApiListing);
        }
        Set<String> newTagNameSet = newTags.stream().map(Tag::getName).collect(Collectors.toSet());
        Set<Tag> distSet = srcTags.stream().filter(tag -> isDistSet(tag, newTagNameSet)).collect(Collectors.toSet());
        Documentation newDocument = new Documentation(
                document.getGroupName(),
                document.getBasePath(),
                distSet,
                newApiListings,
                newResourceListing,
                newProduces,
                newConsumes,
                document.getHost(),
                newSchemes,
                document.getVendorExtensions());

        return newDocument;

    }

    /**
     * 是否是目标set
     *
     * @param tag
     * @param newTagNameSet
     * @return
     */
    private boolean isDistSet(Tag tag, Set<String> newTagNameSet) {
        return newTagNameSet.stream().filter(tagStr -> tag.getName().contains(tagStr)).collect(Collectors.toSet()).size() > 0;
    }

    private void handlerNewTagSet(Set<Tag> newTags, ApiDescription api) {
        List<Operation> operations = api.getOperations();
        for (Operation operation : operations) {
            newTags.addAll(operation.getTags().stream().map(tag -> {
                return new Tag(tag, tag);
            }).collect(Collectors.toSet()));
        }
    }

    /**
     * 是否需存在符合条件的api
     *
     * @param newTags
     * @param api
     * @param keyword
     * @return
     */
    private boolean isExistApi(Set<Tag> newTags, ApiDescription api, String keyword) {
        String path = api.getPath();
        if (path.contains(keyword)) {
            handlerNewTagSet(newTags, api);
            return true;
        }
        List<Operation> operations = api.getOperations();
        for (Operation operation : operations) {
            if (operation.getTags().stream().filter(tag -> tag.contains(keyword)).collect(Collectors.toSet()).size() > 0) {
                newTags.add(new Tag(keyword, keyword));
                return true;
            }
            if (!StringUtils.isBlank(operation.getSummary()) && operation.getSummary().contains(keyword)) {
                handlerNewTagSet(newTags, api);
                return true;
            }
            if (operation.getNotes() != null && operation.getNotes().contains(keyword)) {
                handlerNewTagSet(newTags, api);
                return true;
            }
        }

        return false;
    }

}
