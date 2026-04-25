// Custom Swagger UI configuration to hide unwanted elements
window.onload = function() {
  // Hide servers dropdown
  const hideServers = () => {
    const servers = document.querySelector('.servers');
    const serversWrapper = document.querySelector('.servers-wrapper');
    const serverSelect = document.querySelector('select[aria-label="Servers"]');
    
    if (servers) servers.style.display = 'none';
    if (serversWrapper) serversWrapper.style.display = 'none';
    if (serverSelect) serverSelect.style.display = 'none';
    
    // Hide any element containing "servers"
    document.querySelectorAll('[class*="servers"]').forEach(el => {
      el.style.display = 'none';
    });
  };
  
  // Hide schema examples
  const hideSchemas = () => {
    const models = document.querySelector('.models');
    const modelsWrapper = document.querySelector('.models-wrapper');
    
    if (models) models.style.display = 'none';
    if (modelsWrapper) modelsWrapper.style.display = 'none';
    
    // Hide schema section
    document.querySelectorAll('[class*="models"]').forEach(el => {
      el.style.display = 'none';
    });
  };
  
  // Run immediately and on DOM changes
  hideServers();
  hideSchemas();
  
  // Use MutationObserver to hide elements as they appear
  const observer = new MutationObserver(() => {
    hideServers();
    hideSchemas();
  });
  
  observer.observe(document.body, {
    childList: true,
    subtree: true
  });
  
  // Also run after a delay to catch late-loading elements
  setTimeout(() => {
    hideServers();
    hideSchemas();
  }, 1000);
  
  setTimeout(() => {
    hideServers();
    hideSchemas();
  }, 3000);
};
