package com.painkiller.stepper_client

import com.fkorotkov.kubernetes.rbac.*
import io.fabric8.kubernetes.client.DefaultKubernetesClient

fun bootstrap(applicationName : String) {
  DefaultKubernetesClient().inNamespace("default").use {
    it.rbac().roles().createOrReplace(
      newRole {
        metadata {
          name = applicationName
        }
        rules = listOf(
          newPolicyRule {
            apiGroups = listOf("")
            resources = listOf("services")
            verbs = listOf("create", "delete", "list", "get", "update")
          },
          newPolicyRule {
            apiGroups = listOf("")
            resources = listOf("serviceaccounts")
            verbs = listOf("create", "get", "update")
          },
          newPolicyRule {
            apiGroups = listOf("apps")
            resources = listOf("deployments")
            verbs = listOf("create", "delete", "get", "list", "update")
          }
        )
      }
    )

    it.rbac().roleBindings().createOrReplace(
      newRoleBinding {
        metadata {
          name = applicationName
        }
        subjects = listOf(
          newSubject {
            kind = "ServiceAccount"
            namespace = "default"
            name = "$applicationName-dark"
          },
        )
        roleRef {
          kind = "Role"
          name = applicationName
        }
      }
    )
  }
}