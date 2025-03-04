rootProject.name = "collector"

include(
    ":subproject:boot",
    ":subproject:domain",
    ":subproject:application",
    ":subproject:presentation",
    ":subproject:infrastructure",
    ":subproject:interface",
    ":subproject:interface:channel-collector-interface",
    ":subproject:interface:channel-page-collector-interface",
)
