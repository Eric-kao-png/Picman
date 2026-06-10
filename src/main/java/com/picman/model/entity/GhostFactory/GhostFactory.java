package com.picman.model.entity.GhostFactory;

import java.util.ArrayList;
import java.util.List;

import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Ghost.Ghost;

public interface GhostFactory {
    abstract Ghost createGhost();
}


