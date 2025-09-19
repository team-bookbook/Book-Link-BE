const fs = require('fs');
const path = require('path');
const subPath = process.argv[2];  // booklink 다음 경로: e.g., order
const className = process.argv[3]; // 클래스명: e.g., Order

if (!subPath || !className) {
  console.error('사용법: node generate.js <subPath> <ClassName>');
  process.exit(1);
}
// 도메인 경로를
const baseDir = path.join('src/main/java/com/bookbook/booklink', subPath);
const packageName = baseDir.split(path.sep).join('.').replace(/^src\.main\.java\./, '');

const packages = [
  {
    dir: 'controller',
    suffix: 'Controller',
    annotation: '@RestController',
    import: `import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;`,
    isController: true,
    injectClass: 'Service',
    injectPackage: 'service'
  },
  {
    dir: 'service',
    suffix: 'Service',
    annotation: '@Service',
    import: `import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;`,
    isService: true,
    injectClass: 'Repository',
    injectPackage: 'repository'
  },
  {
    dir: 'repository',
    suffix: 'Repository',
    annotation: '@Repository',
    import: `import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;`,
    isRepository: true
  },
  {
    dir: 'model',
    suffix: '',
    annotation: '@Entity',
    import: `import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;`,
    isEntity: true
  },
  {
    dir: 'model/dto/request',
    suffix: '',
    annotation: '',
    import: '',
    isDto: true
  },
  {
    dir: 'model/dto/response',
    suffix: '',
    annotation: '',
    import: '',
    isDto: true
  }
];

function createDirectories(basePath, structure) {
  structure.forEach(pkg => {
    const packageDir = path.join(basePath, pkg.dir.replace(/\//g, path.sep));
    if (!fs.existsSync(packageDir)) {
      fs.mkdirSync(packageDir, { recursive: true });
      console.log(`Directory created: ${packageDir}`);
    }

    if (pkg.isDto) return; // DTO는 파일 생성 안함

    const classNameWithSuffix = `${className}${pkg.suffix}`;
    const filePath = path.join(packageDir, `${classNameWithSuffix}.java`);
    const packageFull = packageName + `.${pkg.dir.replace(/\//g, '.')}`;

    let imports = pkg.import || '';
    let body = '';
    let declaration = `public class ${classNameWithSuffix}`;

    // 엔티티
    if (pkg.isEntity) {
      imports += `
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;`.trim();

      declaration = `@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${classNameWithSuffix}`;

      body = `
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  `.trim();
    }
    // Repository
    else if (pkg.isRepository) {
      declaration = `public interface ${classNameWithSuffix} extends JpaRepository<${className}, UUID>`;
      body = '';

      imports += `\nimport ${packageName}.model.${className};`;
    }
    // Controller
    else if (pkg.isController) {
      declaration = `@RequiredArgsConstructor\n@RequestMapping("/api/${className.toLowerCase()}")\npublic class ${classNameWithSuffix}`;
      body = `    private final ${className}Service ${className.charAt(0).toLowerCase() + className.slice(1)}Service;`;

      // Controller import 추가
      imports += `\nimport ${packageName}.service.${className}Service;`;
    }
    // Service
    else if (pkg.isService) {
      declaration = `@RequiredArgsConstructor\npublic class ${classNameWithSuffix}`;
      body = `    private final ${className}Repository ${className.charAt(0).toLowerCase() + className.slice(1)}Repository;`;

      // Service import 추가
      imports += `\nimport ${packageName}.repository.${className}Repository;`;
    } else {
      body = '    // Your code here';
    }

    const content = `
package ${packageFull};

${imports}

${pkg.annotation || ''}
${declaration} {
${body}
}
    `.trimStart();

    fs.writeFileSync(filePath, content);
    console.log(`File created: ${filePath}`);
  });
}

createDirectories(baseDir, packages);
