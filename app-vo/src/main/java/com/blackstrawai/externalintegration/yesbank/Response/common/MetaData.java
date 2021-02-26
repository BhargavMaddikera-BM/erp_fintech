package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MetaData {

  @JsonProperty("Links")
  private LinksVo links;

  @JsonProperty("Meta")
  private MetaVo meta;

  public LinksVo getLinks() {
    return links;
  }

  public void setLinks(LinksVo links) {
    this.links = links;
  }

  public MetaVo getMeta() {
    return meta;
  }

  public void setMeta(MetaVo meta) {
    this.meta = meta;
  }
}
