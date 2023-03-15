package com.moseoh.kpa.dialog.domain.model;

import lombok.Getter;

@Getter
public enum FileContent {
    CONTROLLER("package ${package}\n" +
            "\n" +
            "import ${basePackage}.entity.${domainName}\n" +
            "import ${basePackage}.service.${domainName}Service\n" +
            "import org.springframework.web.bind.annotation.RequestMapping\n" +
            "import org.springframework.web.bind.annotation.RestController\n" +
            "\n" +
            "@RestController\n" +
            "@RequestMapping(\"/api/v1/${domain}\")\n" +
            "class ${domainName}Controller(\n" +
            "private val ${domain}Service: ${domainName}Service\n" +
            ") {\n" +
            "}"),
    DTO("package ${package}\n" +
            "\n" +
            "data class ${domainName}Dto(\n" +
            "\n" +
            ") {\n" +
            "}"),
    ENTITY("package ${package}\n" +
            "\n" +
            "import jakarta.persistence.Entity\n" +
            "import jakarta.persistence.Id\n" +
            "import jakarta.persistence.GeneratedValue\n" +
            "import jakarta.persistence.GenerationType\n" +
            "\n" +
            "@Entity()\n" +
            "data class ${domainName}(\n" +
            "    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
            "    val id: Long? = null,\n" +
            ") {\n" +
            "}"),
    REPOSITORY("package ${package}\n" +
            "\n" +
            "import ${basePackage}.entity.${domainName}\n" +
            "import org.springframework.data.jpa.repository.JpaRepository\n" +
            "import org.springframework.stereotype.Repository\n" +
            "\n" +
            "@Repository\n" +
            "interface ${domainName}Repository : JpaRepository<${domainName}, Long>"),
    SERVICE("package ${package}\n" +
            "\n" +
            "import ${basePackage}.entity.repository.${domainName}Repository\n" +
            "import org.springframework.stereotype.Service\n" +
            "\n" +
            "@Service\n" +
            "class ${domainName}Service(\n" +
            "    private val ${domain}Repository: ${domainName}Repository\n" +
            ") {\n" +
            "}");

    private final String content;

    FileContent(String content) {
        this.content = content;
    }
}
