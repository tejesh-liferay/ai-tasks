/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { useCallback, useEffect, useState } from 'react';
import { NavLink, useParams } from 'react-router-dom';

import { JsonEditor } from 'json-edit-react';

import { ROUTE_TASK_LIST } from '../constants/AITasksRoutesConstants';
import { useAITasksContext } from '../contexts/AITasksContext';
import { DnDProvider } from '../contexts/DnDContext';
import { useModal } from '../contexts/ModalContext';
import AITaskEditModal, { AITaskEditModalFooter } from './AITaskEditModal';
import AITaskFlowEditor from './flow/AITaskFlowEditor';
import Alert from './ui/Alert';
import Icon from './ui/Icon';
import Label from './ui/Label';
import LoadingIndicator from './ui/LoadingIndicator';
import NavigationBar from './ui/NavigationBar';
import { TabContent } from './ui/TabContent';
import { Tab, Tabs } from './ui/Tabs';

const AITaskEdit = () => {
  const { fetchTask, selectedTask, setSelectedTask, updateTask, loading, error } =
    useAITasksContext();
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState(0);
  const { setIsModalOpen, setModalTitle, setModalContent, setModalFooter } = useModal();

  const fetchCurrentTask = useCallback(async () => {
    const task = await fetchTask(id);
    setSelectedTask(task);
  }, [id]);

  useEffect(() => {
    fetchCurrentTask();
  }, []);

  const handleConfigurationChange = async ({ newData }) => {
    const updatedTask = {
      ...selectedTask,
      configuration: { ...newData },
    };
    updateTask(updatedTask);
  };

  const handleInputOnChange = (e) => {
    let { name, value } = e.target;
    if (name === 'externalReferenceCode') {
      value = value.trim().replaceAll(' ', '');
    }
    if (name === 'enabled') {
      value = JSON.parse(value);
    }
    const updatedTask = {
      ...selectedTask,
      [name]: value,
    };

    setSelectedTask(updatedTask);
  };

  const handleBasicInfoChanges = async () => {
    const currentSavedTask = await fetchTask(selectedTask.id);
    if (
      currentSavedTask.title !== selectedTask.title ||
      currentSavedTask.externalReferenceCode !== selectedTask.externalReferenceCode ||
      currentSavedTask.enabled !== selectedTask.enabled
    ) {
      updateTask(selectedTask);
    }
  };

  const openModal = (e) => {
    e.preventDefault();
    setModalTitle(`Edit Title and Description`);
    setModalContent(() => <AITaskEditModal />);
    setModalFooter(() => <AITaskEditModalFooter />);
    setIsModalOpen(true);
  };

  if (loading) {
    return (
      <div className={'d-flex justify-content-center align-items-center vh-100'}>
        <LoadingIndicator />
      </div>
    );
  }

  if (error) {
    return <Alert type={'danger'} lead={'Error'} message={error} />;
  }

  if (!selectedTask) {
    return <div>Item not found</div>;
  }

  return (
    <>
      <div class="ai-tasks-navbar-root">
        <NavigationBar>
          <NavLink to={ROUTE_TASK_LIST} className={'btn btn-monospaced btn-sm'}>
            <Icon name={'angle-left'} />
          </NavLink>
          <li class="border-right c-mr-3 c-pr-3 text-left title-description-toolbar-item tbar-item">
            <div>
              <button className={'btn btn-unstyled text-truncate'} onClick={openModal}>
                <p className={'h3'}>{selectedTask.title}</p>
                <i>{selectedTask.description}</i>
              </button>
            </div>
          </li>

          <li class="border-right c-mr-3 c-pr-3 text-left title-description-toolbar-item tbar-item">
            <Label type={'info'} className={'my-2'}>
              {'version ' + selectedTask.version}
            </Label>
          </li>

          <li class="text-3 text-left text-truncate-inline tbar-item tbar-item-expand">
            <div className="text-truncate">
              <strong className="c-mr-1 text-secondary">ID:</strong>
              <span>{selectedTask.id}</span>
            </div>
            <div className="text-truncate">
              <label htmlFor={'erc'} className={'c-mr-1 text-secondary'}>
                ERC:
              </label>
              <input
                id={'externalReferenceCode'}
                name={'externalReferenceCode'}
                className={'px-2'}
                type={'text'}
                value={selectedTask.externalReferenceCode}
                onChange={handleInputOnChange}
                onBlur={() => {
                  handleBasicInfoChanges();
                }}
                onKeyDown={(e) => {
                  if (e.code === 'Enter') {
                    handleBasicInfoChanges();
                  }
                }}
              />
            </div>
          </li>

          <li class="tbar-item">
            <label className="toggle-switch simple-toggle-switch">
              <span className="toggle-switch-label">
                {selectedTask.enabled ? 'Enabled' : 'Disabled'}
              </span>
              <span className="toggle-switch-check-bar">
                <input
                  id="enabled"
                  name="enabled"
                  className="toggle-switch-check"
                  role="switch"
                  type="checkbox"
                  value={selectedTask.enabled}
                  defaultChecked={selectedTask.enabled}
                  onChange={() => {
                    const updatedTask = {
                      ...selectedTask,
                      enabled: !selectedTask.enabled,
                    };
                    setSelectedTask(updatedTask);
                    updateTask(updatedTask);
                  }}
                />
                <span aria-hidden="true" className="toggle-switch-bar">
                  <span className="toggle-switch-handle"></span>
                </span>
              </span>
            </label>
          </li>
        </NavigationBar>
        <Tabs className={'justify-content-center'} onChange={setActiveTab}>
          <Tab id={'flowEditor'} label={'Flow Editor'} />
          <Tab id={'jsonEditor'} label={'JSON Editor'} />
        </Tabs>
      </div>
      <TabContent activeTab={activeTab}>
        <div id={'flowEditor'}>
          <div>
            <DnDProvider>
              <AITaskFlowEditor />
            </DnDProvider>
          </div>
        </div>
        <div id={'jsonEditor'}>
          <JsonEditor
            data={selectedTask.configuration}
            rootName={'configuration'}
            indent={2}
            collapse={2}
            collapseAnimationTime={150}
            maxWidth={'60%'}
            onEdit={handleConfigurationChange}
            onAdd={handleConfigurationChange}
            onDelete={handleConfigurationChange}
          />
        </div>
      </TabContent>
    </>
  );
};

export default AITaskEdit;
