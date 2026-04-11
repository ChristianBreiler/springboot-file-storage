import i18n from "i18next";
import { initReactI18next } from "react-i18next";

const resources = {
  en: {
    translation: {
      folders: "Folders",
      files: "Files",
      createNew: "Create New",
      mainMenu: "Main Menu",
      allFiles: "All Files",
      trash: "Trash",
      viewProfile: "View Profile",
      signOut: "Sign out",
      deletedFiles: "Deleted Files",
      storageManagement: "Storage Management",
      storageManagementInfo:
        "Real-time overview of your drive usage and file distribution.",
      noData: "No data available.",
      usageSummary: "Usage Summary",
      totalFolders: "Total Folders",
      totalFiles: "Total Files",
      largestFiles: "Largest Files in Storage",
      fileName: "File Name",
      size: "Size",
      emailAddress: "Email Address",
      profileCreatedAt: "Profile created at: ",
      editProfile: "Edit Profile",
      newFolder: "New Folder",
      createFolderInfo: "Create a folder for your files",
      createFolder: "Create Folder",
      deleteFolder: "Delete Folder",
      cancel: "Cancel",
      folderNotEmpty:
        "This folder is not empty. You can only delete empty folders at this time.",
      deletePermanently: "Delete Permanently",
      deleteFolderConfirmation:
        'Are you sure you want to delete "{{folderName}}"?',
      folderEmpty: "This Folder is Empty",
      renameFile: "Rename your File",
      search: "Search ...",
      uploadFile: "Upload File"
    },
  },
  de: {
    translation: {
      folders: "Ordner",
      files: "Dateien",
      createNew: "Neu Erstellen",
      mainMenu: "Hauptmenü",
      allFiles: "Alle Dateien",
      trash: "Papierkorb",
      viewProfile: "Profil ansehen",
      signOut: "Abmelden",
      deletedFiles: "Gelöschte Dateien",
      storageManagement: "Speicherverwaltung",
      storageManagementInfo:
        "Echtzeit-Übersicht über Ihre Laufwerksnutzung und Dateiverteilung.",
      noData: "Keine Daten verfügbar.",
      usageSummary: "Nutzungübersicht",
      totalFolders: "Ordner insgesamt",
      totalFiles: "Dateien insgesamt",
      largestFiles: "Größten Dateien im Speicher",
      fileName: "Dateiname",
      size: "Größe",
      emailAddress: "Emailaddresse",
      profileCreatedAt: "Profile erstellt am: ",
      editProfile: "Profil bearbeiten",
      newFolder: "Neuer Ordner",
      createFolderInfo: "Erstell einen neuen Ordner für deine Dateien",
      createFolder: "Ordner erstellen",
      deleteFolder: "Ordner löschen",
      cancel: "Abbrechen",
      folderNotEmpty:
        "Dieser Ordner ist nicht leer. Derzeit können nur leere Ordner gelöscht werden.",
      deletePermanently: "Permanent löschen",
      deleteFolderConfirmation:
        'Sind sie sicher dass sie "{{folderName}}" löschen wollen?',
      folderEmpty: "Dieser Ordner is leer",
      renameFile: "Datei umbenennen",
      search: "Suchen ...",
      uploadFile: "Datei hochladen"
    },
  },
};

i18n.use(initReactI18next).init({
  resources,
  lng: "en",
  interpolation: {
    escapeValue: false,
  },
  react: {
    useSuspense: false,
  },
});

export default i18n;
