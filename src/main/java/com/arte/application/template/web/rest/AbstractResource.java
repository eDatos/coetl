package com.arte.application.template.web.rest;

import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public class AbstractResource {

}
