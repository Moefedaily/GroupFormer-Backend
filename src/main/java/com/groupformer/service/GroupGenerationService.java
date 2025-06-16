package com.groupformer.service;

import com.groupformer.dto.GenerateGroupsRequest;
import com.groupformer.model.GroupDraw;

public interface GroupGenerationService {

    GroupDraw generateGroups(GenerateGroupsRequest request);
}